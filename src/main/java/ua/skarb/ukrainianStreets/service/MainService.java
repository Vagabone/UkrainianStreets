package ua.skarb.ukrainianStreets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.*;
import ua.skarb.ukrainianStreets.service.interfaces.DistrictService;
import ua.skarb.ukrainianStreets.service.interfaces.LocalityService;
import ua.skarb.ukrainianStreets.service.interfaces.RegionService;
import ua.skarb.ukrainianStreets.service.interfaces.StreetService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Central class that step-by-step fill database by data retrieved from Overpass API,
 * starting from regions->districts->regions main city->localities->streets.
 * Also fix errors when overpass API reject http request.
 *
 * @author Vynohradov Evgeniy
 */
@Service
public class MainService {

    /**
     * Query written in Overpass QL(query language) to get all regions for Ukraine.
     */
    private static final String regionQuery = "[out:json][timeout:200];" +
            "rel[name = \"Україна\"][boundary = administrative][admin_level = \"2\"];" +
            "rel(r)[place = \"state\"][name];" +
            "out;";

    /**
     * Template query to get region main city by region overpass id.
     */
    private static final String regionMainCity = "[out:json][timeout:200];" +
            "rel(%s);" +
            "node(r)[place ~ \"town|city|village\"][name];" +
            "out;";

    /**
     * Template query to get districts by region overpass id.
     */
    private static final String districtQuery = "[out:json][timeout:200];" +
            "rel(%s);" +
            "map_to_area -> .obl;" +
            "(rel(area.obl)[admin_level = \"6\"][boundary=\"administrative\"][type=\"boundary\"][name!~\"міська рада$\"][place!~\".\"];" +
            ");" +
            "out;";

    /**
     * Template query to get localities by district overpass id.
     */
    private static final String localityQuery = "[out:json][timeout:200];" +
            "rel(%s);" + //districtId
            "map_to_area -> .district;" +
            "node(area.district)[place ~ \"town|city|village\"][name][~\"^name:prefix*$\"~\".\"];" +
            "out;";

    /**
     * Template query to get streets by locality overpass id.
     */
    private static final String streetQuery = "[out:json][timeout:200];" +
            "node(%s);" + //localityId
            "is_in -> .loc;" +
            "(rel(pivot.loc)[place ~ \"town|city|village\"][name];" +
            "way(pivot.loc)[place ~ \"town|city|village\"][name];);" +
            "map_to_area -> .town;" +
            "way(area.town)[highway][name];" +
            "out;";

    private static final String districtAdminCenter = "[out:json][timeout:200];" +
            "rel(%s);" + //districtId
            "node(r)[name][place];" +
            "out;";

    private OverpassService overpassService;

    private final RegionService regionService;

    private final LocalityService localityService;

    private final StreetService streetService;

    private final DistrictService districtService;

    private final List<Region> errorRegions = new ArrayList<>();

    private final List<Locality> errorLocalities = new ArrayList<>();

    private final List<District> errorDistricts = new ArrayList<>();

    private final List<Region> errorMainCities = new ArrayList<>();

    @Autowired
    public MainService(OverpassService overpassService, RegionService regionService, LocalityService localityService, StreetService streetService, DistrictService districtService) {
        this.overpassService = overpassService;
        this.regionService = regionService;
        this.localityService = localityService;
        this.streetService = streetService;
        this.districtService = districtService;
    }

    public void fillDB() throws FileNotFoundException {
        System.setErr(new PrintStream(new File("log.txt")));
        fillRegions();
        fillDistricts();
        fillDistrictsAdminCenters();
        fillLocalities();
        fillStreets();
        System.out.println("---------------------fill of database is completed-------------------------");
    }


    private void fillRegions() {
        try {
            OverpassNode[] overpassNodes = overpassService.executeQuery(regionQuery);
            regionService.saveRegions(regionService.convert(overpassNodes));
        } catch (IOException e) {
            System.err.println("Error while execute regionQuery");
        }
    }

    private void fillDistricts() {
        List<Region> allRegions = regionService.getAllRegions();
        for (Region region : allRegions) {
            try {
                fillDistrictsForRegion(region);
            } catch (IOException e) {
                System.err.println("Error while execute districtQuery for:" + region);
                errorRegions.add(region);
            }
            try {
                fillRegionMainCity(region);
            } catch (IOException e) {
                System.err.println("Error while execute regionMainCity for:" + region);
                errorMainCities.add(region);
            }
        }
        fixDistricts();
        fixRegionMainCity();
    }


    private void fixDistricts() {
        while (errorRegions.size() != 0) {
            List<Region> readyToRemove = new ArrayList<>();
            for (Region region : errorRegions) {
                try {
                    System.out.println("Try fix: " + region);
                    fillDistrictsForRegion(region);
                    System.out.println("Region fixed " + region);
                    readyToRemove.add(region);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            errorRegions.removeAll(readyToRemove);
        }
    }

    private void fillDistrictsAdminCenters() {
        List<District> allDistricts = districtService.getAllDistricts();
        for (District district : allDistricts) {
            try {
                fillDistrictsAdminCentersForDistrict(district);
            } catch (IOException e) {
                System.err.println("Error while execute districtAdminCenter for: " + district);
                errorDistricts.add(district);
            }
        }
        fixDistrictsAdminCenters();
    }

    private void fillRegionMainCity(Region region) throws IOException {
        OverpassNode[] overpassNodes = overpassService.executeQuery(String.format(regionMainCity, region.getOverpassId()));
        List<Locality> localities = localityService.convert(overpassNodes);
        if (localities.size() > 0) {
            localities.forEach(d -> d.setRegion(region));
            localityService.saveLocalities(localities);
        }
    }

    private void fixRegionMainCity() {
        while (errorMainCities.size() != 0) {
            List<Region> readyToRemove = new ArrayList<>();
            for (Region region : errorMainCities) {
                try {
                    System.out.println("Try fix main city for: " + region);
                    fillRegionMainCity(region);
                    System.out.println("Main city fixed for: " + region);
                    readyToRemove.add(region);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            errorMainCities.removeAll(readyToRemove);
        }

    }

    private void fixDistrictsAdminCenters() {
        while (errorDistricts.size() != 0) {
            List<District> readyToRemove = new ArrayList<>();
            for (District district : errorDistricts) {
                try {
                    System.out.println("Try fix admin center for: " + district);
                    fillDistrictsAdminCentersForDistrict(district);
                    System.out.println("Admin center fixed " + district);
                    readyToRemove.add(district);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            errorDistricts.removeAll(readyToRemove);
        }
    }

    private void fillLocalities() {
        List<District> allDistricts = districtService.getAllDistricts();
        for (District district : allDistricts) {
            try {
                fillLocalitiesForDistrict(district);
            } catch (IOException e) {
                System.err.println("Error while execute localityQuery for: " + district);
                errorDistricts.add(district);
            }
        }
        fixLocalities();
    }

    private void fixLocalities() {
        while (errorDistricts.size() != 0) {
            List<District> readyToRemove = new ArrayList<>();
            for (District district : errorDistricts) {
                try {
                    System.out.println("Try fix: " + district);
                    fillLocalitiesForDistrict(district);
                    System.out.println("District fixed " + district);
                    readyToRemove.add(district);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            errorDistricts.removeAll(readyToRemove);
        }
    }

    private void fillStreets() {
        List<Locality> allLocalities = localityService.getLocalitiesWithoutStreets();
        System.out.println("Count of localities: "+ allLocalities.size());
        for (Locality locality : allLocalities) {
            try {
                System.out.println("Querying streets for locality: "+locality);
                fillStreetsForLocality(locality);
            } catch (IOException e) {
                System.err.println("Error while execute streetQuery for: " + locality);
                errorLocalities.add(locality);
            }
        }
        fixStreets();
    }

    private void fixStreets() {
        while (errorLocalities.size() != 0) {
            List<Locality> readyToRemove = new ArrayList<>();
            for (Locality locality : errorLocalities) {
                try {
                    System.out.println("Try fix: " + locality);
                    fillStreetsForLocality(locality);
                    System.out.println("Locality fixed " + locality);
                    readyToRemove.add(locality);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            errorLocalities.removeAll(readyToRemove);
        }
    }

    private void fillDistrictsForRegion(Region region) throws IOException {
        OverpassNode[] overpassNodes = overpassService.executeQuery(String.format(districtQuery, region.getOverpassId()));
        List<District> districts = districtService.convert(overpassNodes);
        if (districts.size() > 0) {
            districts.forEach(d -> d.setRegion(region));
            districtService.saveDistricts(districts);
        }
    }


    private void fillDistrictsAdminCentersForDistrict(District district) throws IOException {
        OverpassNode[] overpassNodes = overpassService.executeQuery(String.format(districtAdminCenter, district.getOverpassId()));
        List<Locality> localities = localityService.convert(overpassNodes);
        List<Locality> filteredLocalities = localityService.removeExistingLocalities(localities);
        saveFilteredLocalities(filteredLocalities, district);
    }

    private void fillLocalitiesForDistrict(District district) throws IOException {
        OverpassNode[] overpassNodes = overpassService.executeQuery(String.format(localityQuery, district.getOverpassId()));
        List<Locality> localities = localityService.convert(overpassNodes);
        List<Locality> filteredLocalities = localityService.removeExistingLocalities(localities);
        saveFilteredLocalities(filteredLocalities, district);
    }

    private void fillStreetsForLocality(Locality locality) throws IOException {
        OverpassNode[] overpassNodes = overpassService.executeQuery(String.format(streetQuery, locality.getOverpassId()));
        List<Street> streets = streetService.convert(overpassNodes);
        if (streets.size() > 0) {
            streets.forEach(s -> s.setLocality(locality));
            streetService.saveStreets(streets);
        }
    }

    private void saveFilteredLocalities(List<Locality> filteredLocalities, District district) {
        if (filteredLocalities.size() > 0) {
            filteredLocalities.forEach(l -> {
                l.setRegion(district.getRegion());
                l.setDistrict(district);
            });
            localityService.saveLocalities(filteredLocalities);
        }
    }

}

package ua.skarb.ukrainianStreets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.skarb.ukrainianStreets.domain.Locality;
import ua.skarb.ukrainianStreets.domain.Region;
import ua.skarb.ukrainianStreets.domain.Street;
import ua.skarb.ukrainianStreets.service.interfaces.LocalityService;
import ua.skarb.ukrainianStreets.service.interfaces.RegionService;
import ua.skarb.ukrainianStreets.service.interfaces.StreetService;

import java.util.List;

@Controller
@RequestMapping(path = "/test")
public class TestViewController {

    private final RegionService regionService;

    private final LocalityService localityService;

    private final StreetService streetService;

    @Autowired
    public TestViewController(RegionService regionService, LocalityService localityService, StreetService streetService) {
        this.regionService = regionService;
        this.localityService = localityService;
        this.streetService = streetService;
    }

    @GetMapping
    public String getTestPage(Model model){
        List<Region> allRegions = regionService.getAllRegions();
        List<Locality> localitiesByRegionId = localityService.getLocalitiesByRegionId(allRegions.get(0).getId());
        List<Street> streetsByLocalityId = streetService.getStreetsByLocalityId(localitiesByRegionId.get(0).getId());
        model.addAttribute("regionList", allRegions);
        model.addAttribute("localitiesList", localitiesByRegionId);
        model.addAttribute("streetsList", streetsByLocalityId);
        return "test";
    }
}

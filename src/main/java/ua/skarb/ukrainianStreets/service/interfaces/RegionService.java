package ua.skarb.ukrainianStreets.service.interfaces;

import ua.skarb.ukrainianStreets.domain.Region;

import java.util.List;

public interface RegionService extends OverpassConverter<Region> {

    void saveRegions(List<Region> regions);

    List<Region> getAllRegions();
}

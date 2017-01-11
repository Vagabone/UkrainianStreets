package ua.skarb.ukrainianStreets.service.interfaces;

import ua.skarb.ukrainianStreets.domain.Locality;

import java.util.List;

public interface LocalityService extends OverpassConverter<Locality> {

    void saveLocalities(List<Locality> localities);

    List<Locality> getAllLocalities();

    List<Locality> getLocalitiesWithoutStreets();

    List<Locality> getLocalitiesByRegionId(Long regionId);

    List<Locality> removeExistingLocalities(List<Locality> localities);
}

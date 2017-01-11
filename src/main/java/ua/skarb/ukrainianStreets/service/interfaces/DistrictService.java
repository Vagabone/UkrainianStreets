package ua.skarb.ukrainianStreets.service.interfaces;

import ua.skarb.ukrainianStreets.domain.District;

import java.util.List;

public interface DistrictService extends OverpassConverter<District> {

    void saveDistricts(List<District> districts);

    List<District> getAllDistricts();

    List<District> getDistrictsByRegionId(Long regionId);
}

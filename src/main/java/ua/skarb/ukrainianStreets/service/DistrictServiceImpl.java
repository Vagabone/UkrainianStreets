package ua.skarb.ukrainianStreets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.District;
import ua.skarb.ukrainianStreets.domain.OverpassNode;
import ua.skarb.ukrainianStreets.domain.repository.DistrictRepository;
import ua.skarb.ukrainianStreets.service.interfaces.DistrictService;

import java.util.ArrayList;
import java.util.List;


@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public List<District> convert(OverpassNode[] overpassNodes) {
        List<District> districts = new ArrayList<>();
        if (overpassNodes != null && overpassNodes.length != 0) {
            for (OverpassNode node : overpassNodes) {
                if (node != null) {
                    districts.add(convert(node));
                }
            }
        }
        return districts;
    }

    private District convert(OverpassNode node) {
        return new District(node.getId(),node.getTags().get("name"),node.getType());
    }

    @Override
    public void saveDistricts(List<District> districts) {
        if (districts != null && districts.size() > 0) {
            districtRepository.save(districts);
        }
    }

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Override
    public List<District> getDistrictsByRegionId(Long regionId) {
        return districtRepository.findByRegionId(regionId);
    }
}

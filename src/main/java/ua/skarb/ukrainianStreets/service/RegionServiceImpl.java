package ua.skarb.ukrainianStreets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.OverpassNode;
import ua.skarb.ukrainianStreets.domain.Region;
import ua.skarb.ukrainianStreets.domain.repository.RegionRepository;
import ua.skarb.ukrainianStreets.service.interfaces.RegionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Region> convert(OverpassNode[] overpassNodes) {
        List<Region> regions = new ArrayList<>();
        if (overpassNodes != null && overpassNodes.length != 0) {
            for (OverpassNode node : overpassNodes) {
                if (node != null) {
                    regions.add(convert(node));
                }
            }
        }
        return regions;
    }

    private Region convert(OverpassNode node) {
        return new Region(node.getId(), node.getTags().get("name"), node.getType());
    }

    @Override
    public void saveRegions(List<Region> regions) {
        regionRepository.save(regions);
    }

    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
}

package ua.skarb.ukrainianStreets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.Locality;
import ua.skarb.ukrainianStreets.domain.OverpassNode;
import ua.skarb.ukrainianStreets.domain.repository.LocalityRepository;
import ua.skarb.ukrainianStreets.service.interfaces.LocalityService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalityServiceImpl implements LocalityService {

    private final LocalityRepository localityRepository;

    @Autowired
    public LocalityServiceImpl(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    @Override
    public List<Locality> convert(OverpassNode[] overpassNodes) {
        List<Locality> localities = new ArrayList<>();
        if (overpassNodes != null && overpassNodes.length != 0) {
            for (OverpassNode node : overpassNodes) {
                if (node != null) {
                    localities.add(convert(node));
                }
            }
        }
        return localities;
    }

    private Locality convert(OverpassNode node) {
        return new Locality(node.getId(),node.getTags().get("name"), node.getTags().get("name:prefix"), node.getType());
    }

    @Override
    public void saveLocalities(List<Locality> localities) {
        if (localities != null && localities.size() > 0){
            localityRepository.save(localities);
        }
    }

    @Override
    public List<Locality> getAllLocalities() {
        return localityRepository.findAll();
    }

    @Override
    public List<Locality> getLocalitiesWithoutStreets() {
        return localityRepository.getLocalitiesWithoutStreets();
    }

    @Override
    public List<Locality> getLocalitiesByRegionId(Long regionId) {
        return localityRepository.findByRegionIdOrderByName(regionId);
    }

    @Override
    public List<Locality> removeExistingLocalities(List<Locality> localities) {
        if (localities != null && localities.size() > 0){
            List<Locality> readyToRemove = new ArrayList<>();
            List<Locality> localityList = localityRepository.findAll();
            readyToRemove.addAll(localities.stream().filter(locality -> localityList.stream().anyMatch(l -> l.getOverpassId().equals(locality.getOverpassId()))).collect(Collectors.toList()));
            localities.removeAll(readyToRemove);
        }
        return localities;
    }
}

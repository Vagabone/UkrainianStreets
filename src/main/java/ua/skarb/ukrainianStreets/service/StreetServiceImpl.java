package ua.skarb.ukrainianStreets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.skarb.ukrainianStreets.domain.OverpassNode;
import ua.skarb.ukrainianStreets.domain.Street;
import ua.skarb.ukrainianStreets.domain.repository.StreetRepository;
import ua.skarb.ukrainianStreets.service.interfaces.StreetService;

import java.util.ArrayList;
import java.util.List;

@Service
public class StreetServiceImpl implements StreetService {

    private final StreetRepository streetRepository;

    @Autowired
    public StreetServiceImpl(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @Override
    public List<Street> convert(OverpassNode[] overpassNodes) {
        List<Street> streets = new ArrayList<>();
        if (overpassNodes != null && overpassNodes.length != 0) {
            for (OverpassNode node : overpassNodes) {
                if (node != null) {
                    streets.add(convert(node));
                }
            }
        }
        return streets;
    }

    private Street convert(OverpassNode node){
        return new Street(node.getId(),node.getTags().get("name"),node.getType());
    }

    @Override
    public void saveStreets(List<Street> streets) {
        if (streets != null && streets.size() > 0){
            streetRepository.save(streets);
        }
    }

    @Override
    public List<Street> getStreetsByLocalityId(Long localityId) {
        return streetRepository.getGroupedStreetsByLocalityId(localityId);
    }
}

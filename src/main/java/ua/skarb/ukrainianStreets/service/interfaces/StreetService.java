package ua.skarb.ukrainianStreets.service.interfaces;


import ua.skarb.ukrainianStreets.domain.Street;

import java.util.List;

public interface StreetService extends OverpassConverter<Street>{

    void saveStreets(List<Street> streets);

    List<Street> getStreetsByLocalityId(Long localityId);
}

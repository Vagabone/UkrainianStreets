package ua.skarb.ukrainianStreets.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.skarb.ukrainianStreets.domain.Locality;

import java.util.List;

public interface LocalityRepository extends JpaRepository<Locality, Long> {

    List<Locality> findByRegionIdOrderByName(Long regionId);

    /**
     * Method execute native query and get localities without streets.
     *
     * @return a list of {@code Locality} that hasn't any streets
     */
    @Query(value = "SELECT L.* FROM LOCALITIES L LEFT JOIN STREETS S ON (L.ID = S.LOCALITY_ID) WHERE S.LOCALITY_ID IS NULL", nativeQuery = true)
    List<Locality> getLocalitiesWithoutStreets();
}

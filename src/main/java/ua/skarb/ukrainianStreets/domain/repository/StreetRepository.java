package ua.skarb.ukrainianStreets.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.skarb.ukrainianStreets.domain.Street;

import java.util.List;

public interface StreetRepository extends JpaRepository<Street, Long> {

    /**
     * Execute native query and get streets for locality by locality id and grouped result by max street id and name.
     * In overpass API one street can be splitting into several {@code {@link ua.skarb.ukrainianStreets.domain.OverpassNode}},
     * that's why can be duplicates with same street names.
     *
     * @param localityId id of {@code {@link ua.skarb.ukrainianStreets.domain.Locality}}
     * @return a list of streets
     */
    @Query(value = "SELECT S.* FROM STREETS S JOIN (  SELECT MAX (S.ID) MAXID FROM STREETS S WHERE S.LOCALITY_ID = :localityId GROUP BY S.NAME) STR ON (S.ID = STR.MAXID) ORDER BY S.NAME"
            , nativeQuery = true)
    List<Street> getGroupedStreetsByLocalityId(@Param("localityId") Long localityId);
}

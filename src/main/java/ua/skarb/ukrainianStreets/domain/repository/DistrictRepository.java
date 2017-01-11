package ua.skarb.ukrainianStreets.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skarb.ukrainianStreets.domain.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long>{

    List<District> findByRegionId(Long regionId);
}

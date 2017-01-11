package ua.skarb.ukrainianStreets.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.skarb.ukrainianStreets.domain.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}

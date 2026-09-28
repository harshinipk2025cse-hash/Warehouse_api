package com.warehouseslot.repository;

import com.warehouseslot.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByName(String name);
}

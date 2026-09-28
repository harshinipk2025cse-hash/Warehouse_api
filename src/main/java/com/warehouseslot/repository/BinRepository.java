package com.warehouseslot.repository;

import com.warehouseslot.model.Bin;
import com.warehouseslot.model.ZoneType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BinRepository extends JpaRepository<Bin, Long> {
    boolean existsByCode(String code);
    List<Bin> findByZone_TypeOrderByIdAsc(ZoneType type);
}

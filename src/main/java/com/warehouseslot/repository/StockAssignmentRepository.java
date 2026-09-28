package com.warehouseslot.repository;

import com.warehouseslot.model.Bin;
import com.warehouseslot.model.StockAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StockAssignmentRepository extends JpaRepository<StockAssignment, Long> {
    Optional<StockAssignment> findByBinAndItemCode(Bin bin, String itemCode);
    List<StockAssignment> findByItemCodeAndQuantityGreaterThan(String itemCode, int quantity);
}

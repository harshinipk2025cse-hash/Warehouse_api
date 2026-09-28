package com.warehouseslot.repository;

import com.warehouseslot.model.PickList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickListRepository extends JpaRepository<PickList, Long> {
    boolean existsByOrderId(Long orderId);
}

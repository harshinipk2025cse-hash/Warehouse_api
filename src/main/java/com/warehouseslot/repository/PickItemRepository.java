package com.warehouseslot.repository;

import com.warehouseslot.dto.ItemVelocityReport;
import com.warehouseslot.model.PickItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PickItemRepository extends JpaRepository<PickItem, Long> {

    @Query("select new com.warehouseslot.dto.ItemVelocityReport(p.itemCode, sum(p.quantity)) " +
           "from PickItem p where p.picked = true group by p.itemCode order by sum(p.quantity) desc")
    List<ItemVelocityReport> itemVelocity();
}

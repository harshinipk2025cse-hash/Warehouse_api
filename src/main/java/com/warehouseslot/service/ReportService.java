package com.warehouseslot.service;

import com.warehouseslot.dto.BinUtilizationReport;
import com.warehouseslot.dto.ItemVelocityReport;
import com.warehouseslot.repository.BinRepository;
import com.warehouseslot.repository.PickItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Feature 5: bin utilization and item velocity reports. */
@Service
public class ReportService {
    private final BinRepository binRepo;
    private final PickItemRepository pickItemRepo;

    public ReportService(BinRepository binRepo, PickItemRepository pickItemRepo) {
        this.binRepo = binRepo;
        this.pickItemRepo = pickItemRepo;
    }

    public List<BinUtilizationReport> binUtilization() {
        return binRepo.findAll().stream().map(b -> new BinUtilizationReport(
                b.getCode(), b.getZone().getName(), b.getCapacity(), b.getOccupied(),
                b.getCapacity() == 0 ? 0 : Math.round(b.getOccupied() * 10000.0 / b.getCapacity()) / 100.0)).toList();
    }

    public List<ItemVelocityReport> itemVelocity() {
        return pickItemRepo.itemVelocity();
    }
}

package com.warehouseslot.controller;

import com.warehouseslot.dto.BinUtilizationReport;
import com.warehouseslot.dto.ItemVelocityReport;
import com.warehouseslot.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) { this.service = service; }

    @GetMapping("/bin-utilization")
    public List<BinUtilizationReport> binUtilization() { return service.binUtilization(); }

    @GetMapping("/item-velocity")
    public List<ItemVelocityReport> itemVelocity() { return service.itemVelocity(); }
}

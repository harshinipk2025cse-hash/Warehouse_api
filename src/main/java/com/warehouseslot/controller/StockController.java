package com.warehouseslot.controller;

import com.warehouseslot.dto.StockRequest;
import com.warehouseslot.model.StockAssignment;
import com.warehouseslot.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService service;

    public StockController(StockService service) { this.service = service; }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public StockAssignment assign(@Valid @RequestBody StockRequest req) { return service.assign(req); }

    @GetMapping
    public List<StockAssignment> all() { return service.getAll(); }
}

package com.warehouseslot.controller;

import com.warehouseslot.dto.BinRequest;
import com.warehouseslot.dto.ZoneRequest;
import com.warehouseslot.model.Bin;
import com.warehouseslot.model.Zone;
import com.warehouseslot.service.BinService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BinController {
    private final BinService service;

    public BinController(BinService service) { this.service = service; }

    @PostMapping("/zones")
    @ResponseStatus(HttpStatus.CREATED)
    public Zone createZone(@Valid @RequestBody ZoneRequest req) { return service.createZone(req); }

    @GetMapping("/zones")
    public List<Zone> zones() { return service.getZones(); }

    @PostMapping("/bins")
    @ResponseStatus(HttpStatus.CREATED)
    public Bin createBin(@Valid @RequestBody BinRequest req) { return service.createBin(req); }

    @GetMapping("/bins")
    public List<Bin> bins() { return service.getBins(); }

    @GetMapping("/bins/{id}")
    public Bin bin(@PathVariable Long id) { return service.getBin(id); }
}

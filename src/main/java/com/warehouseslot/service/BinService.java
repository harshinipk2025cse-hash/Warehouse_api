package com.warehouseslot.service;

import com.warehouseslot.dto.BinRequest;
import com.warehouseslot.dto.ZoneRequest;
import com.warehouseslot.model.Bin;
import com.warehouseslot.model.Zone;
import com.warehouseslot.exception.BusinessException;
import com.warehouseslot.exception.ResourceNotFoundException;
import com.warehouseslot.repository.BinRepository;
import com.warehouseslot.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/** Feature 1: register zones and bins. */
@Service
public class BinService {
    private final ZoneRepository zoneRepo;
    private final BinRepository binRepo;

    public BinService(ZoneRepository zoneRepo, BinRepository binRepo) {
        this.zoneRepo = zoneRepo;
        this.binRepo = binRepo;
    }

    public Zone createZone(ZoneRequest req) {
        if (zoneRepo.existsByName(req.name()))
            throw new BusinessException("Zone '" + req.name() + "' already exists");
        Zone z = new Zone();
        z.setName(req.name());
        z.setType(req.type());
        z.setRouteOrder(req.routeOrder());
        return zoneRepo.save(z);
    }

    public List<Zone> getZones() 
    { return zoneRepo.findAll(); }

    public Bin createBin(BinRequest req) {
        if (binRepo.existsByCode(req.code()))
            throw new BusinessException("Bin '" + req.code() + "' already exists");
        Zone zone = zoneRepo.findById(req.zoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found: " + req.zoneId()));
        Bin b = new Bin();
        b.setCode(req.code());
        b.setZone(zone);
        b.setCapacity(req.capacity());
        b.setOccupied(0);
        return binRepo.save(b);
    }

    public List<Bin> getBins() { return binRepo.findAll(); }

    public Bin getBin(Long id) {
        return binRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bin not found: " + id));
    }
}

package com.warehouseslot.service;

import com.warehouseslot.dto.StockRequest;
import com.warehouseslot.model.*;
import com.warehouseslot.exception.BusinessException;
import com.warehouseslot.repository.BinRepository;
import com.warehouseslot.repository.StockAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** Feature 2: assign incoming stock to a bin based on item velocity. */
@Service
public class StockService {
    private final BinRepository binRepo;
    private final StockAssignmentRepository stockRepo;

    public StockService(BinRepository binRepo, StockAssignmentRepository stockRepo) {
        this.binRepo = binRepo;
        this.stockRepo = stockRepo;
    }

    @Transactional
    public StockAssignment assign(StockRequest req) {
        int qty = req.quantity();

        // RULE: velocity decides preferred zone (checked BEFORE saving)
        ZoneType preferred = req.velocity() == Velocity.FAST ? ZoneType.NEAR_DISPATCH : ZoneType.FAR;
        ZoneType other = preferred == ZoneType.NEAR_DISPATCH ? ZoneType.FAR : ZoneType.NEAR_DISPATCH;

        Bin bin = findBinWithSpace(preferred, qty)
                .or(() -> findBinWithSpace(other, qty))   // fallback only if preferred zone is full
                .orElseThrow(() -> new BusinessException(
                        "No bin has enough free capacity for " + qty + " units of " + req.itemCode()));

        StockAssignment sa = stockRepo.findByBinAndItemCode(bin, req.itemCode()).orElseGet(() -> {
            StockAssignment n = new StockAssignment();
            n.setItemCode(req.itemCode());
            n.setBin(bin);
            return n;
        });
        sa.setVelocity(req.velocity());
        sa.setQuantity(sa.getQuantity() + qty);

        bin.setOccupied(bin.getOccupied() + qty);
        binRepo.save(bin);
        return stockRepo.save(sa);
    }

    public List<StockAssignment> getAll() { return stockRepo.findAll(); }

    private Optional<Bin> findBinWithSpace(ZoneType type, int qty) {
        return binRepo.findByZone_TypeOrderByIdAsc(type).stream()
                .filter(b -> b.getCapacity() - b.getOccupied() >= qty)
                .findFirst();
    }
}

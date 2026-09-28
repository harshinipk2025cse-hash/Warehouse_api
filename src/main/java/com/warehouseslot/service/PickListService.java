package com.warehouseslot.service;

import com.warehouseslot.dto.OrderRequest;
import com.warehouseslot.model.*;
import com.warehouseslot.exception.BusinessException;
import com.warehouseslot.exception.ResourceNotFoundException;
import com.warehouseslot.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/** Features 3 & 4: orders, pick list generation, and updating bin occupancy on pick. */
@Service
public class PickListService {
    private final OrderRepository orderRepo;
    private final PickListRepository pickListRepo;
    private final StockAssignmentRepository stockRepo;
    private final BinRepository binRepo;

    public PickListService(OrderRepository orderRepo, PickListRepository pickListRepo,
                           StockAssignmentRepository stockRepo, BinRepository binRepo) {
        this.orderRepo = orderRepo;
        this.pickListRepo = pickListRepo;
        this.stockRepo = stockRepo;
        this.binRepo = binRepo;
    }

    public Order createOrder(OrderRequest req) {
        Order order = new Order();
        req.lines().forEach(l -> order.getLines().add(new OrderLine(l.itemCode(), l.quantity())));
        return orderRepo.save(order);
    }

    public List<Order> getOrders() { return orderRepo.findAll(); }

    public Order getOrder(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public PickList getPickList(Long id) {
        return pickListRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pick list not found: " + id));
    }

    /** Feature 3: pick list sorted by zone route order, then bin code, to minimise walking. */
    @Transactional
    public PickList generatePickList(Long orderId) {
        Order order = getOrder(orderId);
        if (pickListRepo.existsByOrderId(orderId))
            throw new BusinessException("Pick list already generated for order " + orderId);

        Map<Long, Integer> allocated = new HashMap<>();   // stockAssignmentId -> qty already used in this list
        List<PickItem> items = new ArrayList<>();

        for (OrderLine line : order.getLines()) {
            List<StockAssignment> stocks = new ArrayList<>(stockRepo.findByItemCodeAndQuantityGreaterThan(line.getItemCode(), 0));
            stocks.sort(Comparator.comparingInt((StockAssignment s) -> s.getBin().getZone().getRouteOrder())
                    .thenComparing(s -> s.getBin().getCode()));

            int remaining = line.getQuantity();
            for (StockAssignment s : stocks) {
                if (remaining == 0) break;
                int available = s.getQuantity() - allocated.getOrDefault(s.getId(), 0);
                int take = Math.min(available, remaining);   // RULE: never more than the bin holds
                if (take <= 0) continue;

                PickItem p = new PickItem();
                p.setItemCode(s.getItemCode());
                p.setQuantity(take);
                p.setBin(s.getBin());
                p.setStockAssignment(s);
                items.add(p);
                allocated.merge(s.getId(), take, Integer::sum);
                remaining -= take;
            }
            if (remaining > 0)
                throw new BusinessException("Insufficient stock for item '" + line.getItemCode()
                        + "': short by " + remaining + " units");
        }

        // walking route: zone route order, then bin code
        items.sort(Comparator.comparingInt((PickItem p) -> p.getBin().getZone().getRouteOrder())
                .thenComparing(p -> p.getBin().getCode()));

        PickList pl = new PickList();
        pl.setOrder(order);
        pl.setItems(items);
        order.setStatus(OrderStatus.PICK_LIST_CREATED);
        orderRepo.save(order);
        return pickListRepo.save(pl);
    }

    /** Feature 4: picker confirms the list -> stock and bin occupancy go down. All-or-nothing. */
    @Transactional
    public PickList completePicking(Long pickListId) {
        PickList pl = getPickList(pickListId);
        if (pl.getStatus() == PickListStatus.COMPLETED)
            throw new BusinessException("Pick list " + pickListId + " is already completed");

        for (PickItem item : pl.getItems()) {
            StockAssignment sa = item.getStockAssignment();
            Bin bin = item.getBin();
            // RULE re-checked at pick time: quantity can't exceed what the bin holds now
            if (item.getQuantity() > sa.getQuantity())
                throw new BusinessException("Cannot pick " + item.getQuantity() + " of '" + item.getItemCode()
                        + "' from bin " + bin.getCode() + ": only " + sa.getQuantity() + " available");
            sa.setQuantity(sa.getQuantity() - item.getQuantity());
            bin.setOccupied(bin.getOccupied() - item.getQuantity());
            item.setPicked(true);
            stockRepo.save(sa);
            binRepo.save(bin);
        }
        pl.setStatus(PickListStatus.COMPLETED);
        pl.getOrder().setStatus(OrderStatus.FULFILLED);
        orderRepo.save(pl.getOrder());
        return pickListRepo.save(pl);
    }
}

package com.warehouseslot.controller;

import com.warehouseslot.dto.OrderRequest;
import com.warehouseslot.model.Order;
import com.warehouseslot.model.PickList;
import com.warehouseslot.service.PickListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final PickListService service;

    public OrderController(PickListService service) { this.service = service; }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody OrderRequest req) { return service.createOrder(req); }

    @GetMapping("/orders")
    public List<Order> orders() { return service.getOrders(); }

    @GetMapping("/orders/{id}")
    public Order order(@PathVariable Long id) { return service.getOrder(id); }

    /** Generate the optimized pick list for an order. */
    @PostMapping("/orders/{id}/picklist")
    @ResponseStatus(HttpStatus.CREATED)
    public PickList generate(@PathVariable Long id) { return service.generatePickList(id); }

    @GetMapping("/picklists/{id}")
    public PickList pickList(@PathVariable Long id) { return service.getPickList(id); }

    /** Confirm picking: reduces stock and bin occupancy. */
    @PutMapping("/picklists/{id}/pick")
    public PickList pick(@PathVariable Long id) { return service.completePicking(id); }
}

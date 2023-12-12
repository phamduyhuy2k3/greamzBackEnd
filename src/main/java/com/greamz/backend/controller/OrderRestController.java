package com.greamz.backend.controller;

import com.greamz.backend.dto.order_detail.OrderDetailsDTO;
import com.greamz.backend.model.Orders;
import com.greamz.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService service;

    @GetMapping("/getRevenueForCurrentDay")
    public ResponseEntity<Double> getRevenueForCurrentDay() {
        return ResponseEntity.ok(service.findRevenueForCurrentDay());
    }

    @GetMapping("/findAll")
    public ResponseEntity<Iterable<Orders>> findAll() {
        List<Orders> orders = service.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<Orders> findById(@PathVariable("id") UUID id) {
        try {
            Orders order = service.findById(id);
            return ResponseEntity.ok(order);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public Orders getOne(@PathVariable("id") UUID id) {
        return service.findById(id);
    }

    @GetMapping("/findByOrder/{id}")
    public ResponseEntity<List<OrderDetailsDTO>> findByOrder(@PathVariable("id") UUID id) {
        try {
            List<OrderDetailsDTO> orders = service.findOrderDetailsById(id);
            return ResponseEntity.ok(orders);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/save")
//    public Orders save(@RequestBody Orders order) {
//        return service.saveOrder(order);
//    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @GetMapping("/findAllPagination")
    public ResponseEntity<Iterable<Orders>> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Iterable<Orders> orders = service.findAllPagination(PageRequest.of(page, size));
        return ResponseEntity.ok(orders);
    }
}

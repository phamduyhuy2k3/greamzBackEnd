package com.greamz.backend.controller;

import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/")
    public ResponseEntity<?> getOrderByAccountId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @AuthenticationPrincipal UserPrincipal account) {
        return ResponseEntity.ok(orderService.getAllOrdersByAccountId(account.getId(), page, size));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrderByOrdersStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @AuthenticationPrincipal UserPrincipal account) {
        if(status.equals("ALL")){
            return ResponseEntity.ok(orderService.getAllOrdersByAccountId(account.getId(), page, size));

        }
        return ResponseEntity.ok(orderService.getAllOrdersByOrdersStatus(status,account.getId(), page, size));
    }
    @GetMapping("/{orderId}/detail")
    public ResponseEntity<?> getOrderDetailByOrderId(
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getAllOrdersDetailByOrderId(orderId));
    }
    @GetMapping("/game-library")
    public ResponseEntity<?> getGamesThatUserBought(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal UserPrincipal account) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ResponseEntity.ok(orderService.getGamesThatUserBought(account.getId(),pageable));
    }
}

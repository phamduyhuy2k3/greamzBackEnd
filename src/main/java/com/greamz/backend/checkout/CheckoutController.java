package com.greamz.backend.checkout;

import com.greamz.backend.model.Orders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    @PostMapping("/placeorder")
    public void checkout(@RequestBody Orders orders, HttpServletRequest request, HttpServletResponse response) {
        checkoutService.placeOrder(orders, request, response);
    }
    @PostMapping("/saveOrder")
    public ResponseEntity<UUID> saveOrder(@RequestBody Orders orders) {
        return ResponseEntity.ok(checkoutService.saveOrder(orders));
    }

}

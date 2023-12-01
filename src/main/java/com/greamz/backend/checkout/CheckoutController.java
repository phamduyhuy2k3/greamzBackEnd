package com.greamz.backend.checkout;

import com.fasterxml.jackson.databind.JsonNode;
import com.greamz.backend.model.Orders;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.OrderService;
import com.greamz.backend.util.GlobalState;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderService orderService;
    @PostMapping("/placeorder")
    public ResponseEntity<?> checkout(
            @RequestBody Orders orders,
            HttpServletRequest request,
            HttpServletResponse response
            ) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Object data= checkoutService.placeOrder(orders, request,response );

        return ResponseEntity.ok(data);
    }
    @PostMapping("/saveOrder")
    public ResponseEntity<UUID> saveOrder(@RequestBody Orders orders) {
        return ResponseEntity.ok(orderService.saveOrder(orders));
    }
    @GetMapping("/callback")
    public void callback(@RequestParam String orderId,HttpServletResponse response) throws IOException {
        checkoutService.callback(UUID.fromString(orderId),response,true);
    }
    @GetMapping("/callback/client")
    public ResponseEntity<String> callbackClient(@RequestParam String orderId,HttpServletResponse response) throws IOException {
      return  ResponseEntity.ok(checkoutService.callbackFromClient(UUID.fromString(orderId),response));
    }
    @GetMapping("/callback/client")
    public ResponseEntity<String> callbackClient(@RequestParam String orderId,HttpServletResponse response) throws IOException {
      return  ResponseEntity.ok(checkoutService.callbackFromClient(UUID.fromString(orderId),response));
    }
    @GetMapping("/failed")
    public void failed(@RequestParam("orderId") UUID orderId,HttpServletResponse response) throws IOException {
        checkoutService.failed(orderId);
        response.sendRedirect(GlobalState.FRONTEND_URL+"/order/failed");
    }

}

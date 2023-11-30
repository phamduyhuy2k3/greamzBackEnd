package com.greamz.backend.checkout.paypal;

import com.fasterxml.jackson.databind.JsonNode;
import com.greamz.backend.model.Orders;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/payment/paypal")

public class PaypalController {
    @Autowired
    private PaypalService paypalService;

    @PostMapping(value = "/init")
    public ResponseEntity<?> createPayment(@RequestBody JsonNode order) {
         return   ResponseEntity.ok(paypalService.createOrder(order));

    }
    @PostMapping(value = "/capture")
    public void capturePayment(@RequestParam String paypalOrderId, @RequestParam UUID orderId, HttpServletResponse servletResponse) throws IOException {
      paypalService.capturePayment(paypalOrderId,orderId,servletResponse);
    }
//    @PostMapping(value = "/generateAcessToken")
//    public ResponseEntity<?> generateAccessToken() {
//        return ResponseEntity.ok(paypalService.generateAccessToken());
//    }
}

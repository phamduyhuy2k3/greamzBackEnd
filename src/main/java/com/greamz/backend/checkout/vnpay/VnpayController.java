package com.greamz.backend.checkout.vnpay;

import com.greamz.backend.model.Orders;
import com.greamz.backend.model.OrdersStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment/vnpay")
@RequiredArgsConstructor
public class VnpayController {
    private final VnpayService vnpayService;
    @PostMapping("/return")
    public ResponseEntity<?> returnVnpay( HttpServletRequest request, HttpServletResponse response) {
        OrdersStatus ordersStatus= vnpayService.returnUrl(request,response);
        if(ordersStatus==OrdersStatus.SUCCESS){
            return ResponseEntity.ok("Thanh toán thành công");
        }else {
            return ResponseEntity.status(400).body("Thanh toán thất bại");
        }
    }
}

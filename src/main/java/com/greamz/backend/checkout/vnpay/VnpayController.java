package com.greamz.backend.checkout.vnpay;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/vnpay")
@RequiredArgsConstructor
public class VnpayController {
    private final VnpayService vnpayService;

    @GetMapping("/call-back")
    public void returnVnpay(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException, ChangeSetPersister.NotFoundException {
       vnpayService.returnUrl(queryParams,response);

    }
}

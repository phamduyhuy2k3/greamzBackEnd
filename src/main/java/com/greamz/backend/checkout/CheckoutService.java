package com.greamz.backend.checkout;

import com.greamz.backend.checkout.momo.MomoService;
import com.greamz.backend.checkout.paypal.PaypalService;
import com.greamz.backend.checkout.vnpay.VnpayService;
import com.greamz.backend.model.Orders;
import com.greamz.backend.model.PAYMENTMETHOD;
import com.greamz.backend.repository.IOrderRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final IOrderRepo orderRepo;
    private final VnpayService vnpayService;
    private final PaypalService paypalService;
    private final MomoService momoService;
    private final RestTemplate restTemplate;
    public void placeOrder(Orders order, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(order.getPaymentmethod().name());
        System.out.println("user id: " + order.getAccount().getId());
        switch (order.getPaymentmethod()) {

            case VNPAY:
                try {
                    String paymentUrl = vnpayService.createPaymentUrl(order, request, response);
                    System.out.println(paymentUrl);
                    restTemplate.getForObject(paymentUrl, String.class);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case PAYPAL:

                break;
            case MOMO:

                break;
            default:
                throw new IllegalStateException("Please select a payment method: " + order.getPaymentmethod());
        }
    }
    @Transactional
    public UUID saveOrder(Orders orders) {
       Orders orders1= orderRepo.saveAndFlush(orders);
       return orders1.getId();
    }
}



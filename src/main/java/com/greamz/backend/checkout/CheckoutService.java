package com.greamz.backend.checkout;

import com.greamz.backend.checkout.momo.MomoService;
import com.greamz.backend.checkout.paypal.PaypalService;
import com.greamz.backend.checkout.vnpay.VnpayService;
import com.greamz.backend.model.Orders;
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
    @Transactional
    public String placeOrder(Orders order, HttpServletRequest request, HttpServletResponse response) {
        switch (order.getPaymentmethod()) {
            case VNPAY:
                try {
                    return vnpayService.createPaymentUrl(order, request, response);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            case PAYPAL:

                break;
            case MOMO:

                break;
            default:
                throw new IllegalStateException("Please select a payment method: " + order.getPaymentmethod());
        }

        return "redirect:/";
    }
    @Transactional
    public UUID saveOrder(Orders orders) {
       Orders orders1= orderRepo.saveAndFlush(orders);
       return orders1.getId();
    }
//    @Transactional
//    public UUID updateStatusOrder(Orders orders) {
//        orderRepo.findById(orders.getId()).orElseThrow()
//                .setOrdersStatus(PAID);
//        Orders orders1= orderRepo.saveAndFlush(orders);
//        return orders1.getId();
//    }
//    @Transactional
//    public boolean updateStatusOrderCancel(UUID id) {
//        orderRepo.delete(orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found")));
//        return true;
//    }
}



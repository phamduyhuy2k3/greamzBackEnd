package com.greamz.backend.checkout;

import com.greamz.backend.checkout.momo.MomoService;
import com.greamz.backend.checkout.paypal.PaypalService;
import com.greamz.backend.checkout.vnpay.VnpayService;
import com.greamz.backend.model.Orders;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.IGameRepo;
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
    private final IGameRepo gameRepo;
    private final MomoService momoService;
    private final IAccountRepo accountRepo;
    @Transactional
    public Object placeOrder(Orders orders, HttpServletRequest request) throws IOException {

        orders.setOrdersStatus(OrdersStatus.PROCESSING);
        Orders orderSaved= orderRepo.saveAndFlush(orders);
        switch (orders.getPaymentmethod()) {
            case VNPAY:
                try {
                    return vnpayService.createPaymentUrl(orderSaved, request);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            case PAYPAL:
                return CheckOutResponse.builder()
                        .payUrl("")
                        .orderId(orderSaved.getId())
                        .build();
            case MOMO:

                break;
            default:
                throw new IllegalStateException("Please select a payment method: " + orders.getPaymentmethod());
        }

        return "https://greamz.games";
    }


    @Transactional(readOnly = true)
    public Orders getOrder(UUID orderId) {
        Orders orders= orderRepo.findById(orderId).orElseThrow();
        orders.setOrdersDetails(null);

        return orders;
    }
    @Transactional
    public void failed(UUID orderId) {
        Orders orders= orderRepo.findById(orderId).orElseThrow();
        orders.setOrdersStatus(OrdersStatus.FAILED);
        orderRepo.saveAndFlush(orders);
    }
}



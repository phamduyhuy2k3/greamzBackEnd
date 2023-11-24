package com.greamz.backend.checkout;

import com.greamz.backend.checkout.momo.MomoService;
import com.greamz.backend.checkout.paypal.PaypalService;
import com.greamz.backend.checkout.vnpay.VnpayService;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Orders;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.repository.IOrderRepo;
import com.greamz.backend.service.GameModelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final IOrderRepo orderRepo;
    private final VnpayService vnpayService;
    private final GameModelService gameModelService;
    private final MomoService momoService;
    private final IAccountRepo accountRepo;
    @Transactional
    public Object placeOrder(Orders orders, HttpServletRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        orders.setOrdersStatus(OrdersStatus.PROCESSING);
        orders.getOrdersDetails().forEach(ordersDetail -> {
            ordersDetail.setGame(gameModelService.findGameByAppid(ordersDetail.getGame().getAppid()));
        });
        Orders orderSaved= orderRepo.save(orders);
        AccountModel accountModel= accountRepo.findById(orders.getAccount().getId()).orElseThrow();
        switch (orders.getPaymentmethod()) {
            case VNPAY:
                try {
                    return

                            vnpayService.createPaymentUrl(orderSaved, request);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            case PAYPAL:
                return CheckOutResponse.builder()
                        .payUrl("")
                        .orderId(orderSaved.getId())
                        .build();
            case MOMO:
                return momoService.createMomoPayment(orderSaved,accountModel.getFullname() );

            default:
                throw new IllegalStateException("Please select a payment method: " + orders.getPaymentmethod());
        }

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



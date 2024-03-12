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
import com.greamz.backend.service.*;
import com.greamz.backend.util.GlobalState;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final IOrderRepo orderRepo;
    private final OrderService orderService;
    private final VnpayService vnpayService;
    private final GameModelService gameModelService;
    private final MomoService momoService;
    private final IAccountRepo accountRepo;
    private final CartService cartService;
    private final GlobalState globalState;
    @Transactional
    public Object placeOrder(Orders orders, HttpServletRequest request,HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        orders.setOrdersStatus(OrdersStatus.PROCESSING);
        orders.getOrdersDetails().forEach(ordersDetail -> {
            ordersDetail.setGame(gameModelService.findGameByAppid(ordersDetail.getGame().getAppid()));
        });
        Orders orderSaved= orderRepo.save(orders);
        AccountModel accountModel= accountRepo.findById(orders.getAccount().getId()).orElseThrow(NoSuchAlgorithmException::new);
        switch (orders.getPaymentmethod()) {
            case VNPAY -> {
                try {
                    return

                            vnpayService.createPaymentUrl(orderSaved, request);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            case PAYPAL, SOLANA -> {
                return CheckOutResponse.builder()
                        .payUrl("")
                        .orderId(orderSaved.getId())
                        .build();
            }
            case MOMO -> {
                return momoService.createMomoPayment(orderSaved, accountModel.getFullname());
            }
            case FREE -> {
                if (orders.getTotalPrice() <= 0) {
                    return CheckOutResponse.builder()
                            .payUrl("")
                            .orderId(orderSaved.getId())
                            .build();
                } else {
                    throw new IllegalStateException("Please select a payment method: " + orders.getPaymentmethod());
                }
            }
            default -> throw new IllegalStateException("Please select a payment method: " + orders.getPaymentmethod());
        }

    }


    public void callback(UUID orderId, HttpServletResponse response,boolean isRedirect) throws IOException {
        Orders orders = orderService.getOrdersById(orderId);
        orders.setOrdersStatus(OrdersStatus.SUCCESS);
        orderService.saveOrder(orders);
        gameModelService.updateStockForGameFromOrder(orders.getOrdersDetails());
        AccountModel accountModel= accountRepo.findById(orders.getAccount().getId()).orElseThrow();
        BigDecimal balance= BigDecimal.valueOf(orders.getTotalPrice());
        balance.divide(BigDecimal.valueOf(100000));

        accountModel.setBalance(accountModel.getBalance().add(balance));
        accountRepo.save(accountModel);
        cartService.deleteAllCart(accountModel.getId());
        if(isRedirect) response.sendRedirect(globalState.FRONTEND_URL+"/order/success?orderId="+orderId);
    }
    public String callbackFromClient(UUID orderId, HttpServletResponse response) throws IOException {
        Orders orders = orderService.getOrdersById(orderId);
        orders.setOrdersStatus(OrdersStatus.SUCCESS);
        orderService.saveOrder(orders);
        gameModelService.updateStockForGameFromOrder(orders.getOrdersDetails());
        AccountModel accountModel= accountRepo.findById(orders.getAccount().getId()).orElseThrow();
        BigDecimal balance= BigDecimal.valueOf(orders.getTotalPrice());
        balance.divide(BigDecimal.valueOf(10000));
        accountModel.setBalance(accountModel.getBalance().add(balance));
        accountRepo.save(accountModel);
        cartService.deleteAllCart(accountModel.getId());

        return "/order/success?orderId="+orderId;
    }
    @Transactional
    public void failed(UUID orderId,OrdersStatus ordersStatus) {
        Orders orders= orderRepo.findById(orderId).orElseThrow();
        orders.setOrdersStatus(ordersStatus);
        orderRepo.saveAndFlush(orders);
    }
}



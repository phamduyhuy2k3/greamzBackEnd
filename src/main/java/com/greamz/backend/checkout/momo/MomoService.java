package com.greamz.backend.checkout.momo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Orders;
import com.greamz.backend.repository.IOrderRepo;
import com.greamz.backend.util.GlobalState;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MomoService {
    @Autowired
    private Environment env;
    @Autowired
    private IOrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    public MomoResponse createMomoPayment(Orders order, AccountModel accountModel)
            throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        String endPoint = env.getProperty("payment.momo.endpoint");
        String ipnUrl = env.getProperty("payment.momo.ipnUrl");


        String partnerCode = env.getProperty("payment.momo.partner-code");
        String accessKey = env.getProperty("payment.momo.access-key");
        String secretKey = env.getProperty("payment.momo.secret-key");

        String requestType = "captureWallet";

        String redirectUrl = "http://localhost:8080/api/v1/payment/momo/momo-result";

        String orderInfo = "Khách hàng: " + accountModel.getFullname() + " thanh toán";
        if(order.getTotalPrice() >50000000d){
            order.setTotalPrice(50_000_000d);
        }
        String amount = String.valueOf(Math.round(order.getTotalPrice())).replaceAll("[^\\d]", ""); // Xóa dấu phẩy
        String orderId = order.getId().toString();
        String requestId = order.getId().toString();
        String extraData = "";

        List<MomoItem> items =
                order.getOrdersDetails().stream()
                        .map(
                                x ->MomoItem.builder()
                                        .imageUrl(x.getGame().getHeader_image())
                                        .name(x.getGame().getName())
                                        .quantity(x.getQuantity())
                                        .price(x.getGame().getPrice())
                                        .totalPrice(BigDecimal.valueOf(x.getGame().getPrice() * x.getQuantity()))
                                        .build()
                        )
                        .toList();

        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        MomoSecurity crypto = new MomoSecurity();
        // Sign signature SHA256
        String signature = crypto.signSHA256(rawHash, secretKey);

        MomoRequest momoMessage = new MomoRequest();
        momoMessage.setPartnerCode(partnerCode);
        momoMessage.setPartnerName("Steam");
        momoMessage.setStoreId("Steam");
        momoMessage.setRequestId(requestId);
        momoMessage.setAmount(amount);
        momoMessage.setOrderId(String.valueOf(orderId));
        momoMessage.setOrderInfo(orderInfo);
        momoMessage.setRedirectUrl(redirectUrl);
        momoMessage.setIpnUrl(ipnUrl);
        momoMessage.setLang("vi");
        momoMessage.setExtraData(extraData);
        momoMessage.setRequestType(requestType);
        momoMessage.setItems(items);
        momoMessage.setSignature(signature);

        String result = restTemplate.postForObject(endPoint,
                momoMessage, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(result, MomoResponse.class);
    }
    public void momoResult(MomoResult response, HttpServletResponse httpServletResponse) throws IOException {
        Optional<Orders> ordersOptional = orderRepo.findById(UUID.fromString(response.getOrderId()));
        if(ordersOptional.isPresent()){
            Orders order = ordersOptional.get();
            if(response.getResultCode() == 0){
                order.setOrdersStatus(OrdersStatus.SUCCESS);
                Orders orders= orderRepo.save(order);
                httpServletResponse.sendRedirect(GlobalState.FRONTEND_URL+"/order/success?orderId="+orders.getId());
            }else {
                order.setOrdersStatus(OrdersStatus.FAILED);
                Orders orders= orderRepo.save(order);
                httpServletResponse.sendRedirect("http://localhost:8080/api/v1/checkout/failed?orderId="+orders.getId());
            }
        }else {
            httpServletResponse.sendRedirect(GlobalState.FRONTEND_URL+"/order/failed?message=Order not found");
        }


    }
}

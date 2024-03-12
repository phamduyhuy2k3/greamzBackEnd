package com.greamz.backend.checkout.paypal;

import com.fasterxml.jackson.databind.JsonNode;
import com.greamz.backend.checkout.CheckoutService;
import com.greamz.backend.model.Orders;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.service.GameModelService;
import com.greamz.backend.service.OrderService;
import com.greamz.backend.util.GlobalState;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaypalService {
    private final OrderService orderService;
    private final GameModelService gameModelService;
    private final CheckoutService checkoutService;
    private final GlobalState globalState;
    private final RestTemplate restTemplate;
    private final String  BASE = "https://api-m.sandbox.paypal.com";
    @Value("${application.payment.paypal.client-id}")
    private String clientId ;
    @Value("${application.payment.paypal.client-sec}")
    private String secretId;

    private String getAuth() {
        String auth = clientId + ":" + secretId;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }
    public String generateAccessToken() {
        String auth = this.getAuth();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");
        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE +"/v1/oauth2/token",
                request,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("GET TOKEN: SUCCESS!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            log.info("GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }

    public void capturePayment(String orderId, UUID orders, HttpServletResponse servletResponse) throws IOException {
        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );
        Orders order = orderService.getOrdersById(orders);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            order.setOrdersStatus(OrdersStatus.SUCCESS);
            orderService.saveOrder(order);
            gameModelService.updateStockForGameFromOrder(order.getOrdersDetails());
            String redirectURL = globalState.FRONTEND_URL + "/order/success?orderId=" + order.getId();
            servletResponse.setStatus(201);
            servletResponse.getWriter().write("{\"redirectURL\": \"" + redirectURL + "\"}");

        } else {
            order.setOrdersStatus(OrdersStatus.FAILED);
            orderService.saveOrder(order);
            String requestUrl = String.format("/api/v1/checkout/failed?orderId=%s&status=%s", order.getId(), OrdersStatus.FAILED);
            restTemplate.postForObject( requestUrl, null, String.class);

            servletResponse.setStatus(400);
            String redirectURL = globalState.FRONTEND_URL + "/order/failed?orderId=" + order.getId();

            servletResponse.getWriter().write("{\"redirectURL\": \"" + redirectURL + "\"}");

        }
    }
    public Object createOrder(JsonNode paypalCreateOrderRequest) {
        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(paypalCreateOrderRequest, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            log.info("ORDER CREATED");
            return response.getBody();
        } else {
            log.info("FAILED CREATING ORDER");
            return "Unavailable to get CAPTURE ORDER, STATUS CODE " + response.getStatusCode();
        }

    }
}

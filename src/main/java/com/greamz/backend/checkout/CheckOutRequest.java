package com.greamz.backend.checkout;


import com.greamz.backend.checkout.paypal.PaypalCreateOrderRequest;
import com.greamz.backend.model.Orders;
import lombok.Data;

@Data
public class CheckOutRequest {
    private Orders orders;
    private String paymentMethod;
    private PaypalCreateOrderRequest paypalOrder;
}

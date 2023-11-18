package com.greamz.backend.checkout.momo;

import lombok.Data;

@Data
public class MomoResponse {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private Long responseTime;
    private String message;
    private Integer resultCode;
    private String payUrl;
    private String deeplink;
    private String applink;
    private String qrCodeUrl;
    private String deeplinkMiniApp;
    private String bindingUrl;
    private String partnerClientId;
    private Long transId;
    private String deeplinkWebInApp;
    private String shortLink;
}

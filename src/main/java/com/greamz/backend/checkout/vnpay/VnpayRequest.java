package com.greamz.backend.checkout.vnpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VnpayRequest {
    private String vnp_Version;
    private String vnp_Command;
    private String vnp_TmnCode;
    private String vnp_Amount;
    private String vnp_CurrCode;
    private String vnp_TxnRef;
    private String vnp_OrderInfo;
    private String vnp_OrderType;
    private String vnp_Locale;
    private String vnp_ReturnUrl;
    private String vnp_IpAddr;
    private String vnp_CreateDate;
    private String vnp_BankCode;
    private String vnp_TransactionNo;
    private String vnp_TransDate;
    private String vnp_TransactionStatus;
    private String vnp_Message;
    private String vnp_SecureHash;
    private String vnp_SecureHashType;


}

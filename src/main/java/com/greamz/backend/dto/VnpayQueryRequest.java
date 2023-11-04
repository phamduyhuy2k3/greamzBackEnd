package com.greamz.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VnpayQueryRequest {
    private String vnp_RequestId;
    private String vnp_Version;
    private String vnp_Command;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private String vnp_OrderInfo;
    private String vnp_TransactionNo;
    private String vnp_TransactionDate;
    private String vnp_CreateDate;
    private String vnp_IpAddr;
    private String vnp_SecureHash;

}

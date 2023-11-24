package com.greamz.backend.dto.order;

import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.enumeration.PAYMENTMETHOD;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private UUID id;
    private Double totalPrice;
    private PAYMENTMETHOD paymentmethod;
    private OrdersStatus ordersStatus;
    private Date createdAt;
}

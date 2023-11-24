package com.greamz.backend.dto.order_detail;

import com.greamz.backend.dto.account.AccountBasicDTO;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.order.OrderDTO;
import com.greamz.backend.dto.voucher.VoucherOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDTO {
    private Long id;
    private OrderDTO order;
    private GameBasicDTO game;
    private AccountBasicDTO account;
    private VoucherOrderDTO voucher;
    private Integer quantity;
    private Double price;
}

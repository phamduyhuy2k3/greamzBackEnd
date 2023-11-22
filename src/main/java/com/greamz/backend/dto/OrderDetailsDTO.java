package com.greamz.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

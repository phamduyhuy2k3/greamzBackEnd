package com.greamz.backend.dto;

import com.greamz.backend.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailDTO {
    private Integer id;
    private String username;
    private String email;
    private String fullname;
    private String photo;
    private Role role;
    private VoucherOrderDTO voucher;
    private ReviewBasicDTO review;
    private OrderDTO order;
}

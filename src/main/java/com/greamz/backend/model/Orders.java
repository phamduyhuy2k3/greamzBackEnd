package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Orders extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private AccountModel account;
    private Double totalPrice;
    @OneToMany(mappedBy = "orders",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private List<OrdersDetail> ordersDetails;
    @Enumerated(EnumType.STRING)
    private PAYMENTMETHOD paymentmethod;
    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;
    @ManyToOne
    private Voucher voucher;
}

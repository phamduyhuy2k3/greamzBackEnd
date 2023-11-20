package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.enumeration.PAYMENTMETHOD;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonManagedReference
    private List<OrdersDetail> ordersDetails;
    @Enumerated(EnumType.STRING)
    private PAYMENTMETHOD paymentmethod;
    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;
    @ManyToOne
    private Voucher voucher;
}

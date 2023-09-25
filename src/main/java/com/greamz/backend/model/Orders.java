package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JsonBackReference(value = "userOrders")
    private AccountModel account;
    private Double totalPrice;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderedAt;
    @OneToMany(mappedBy = "orders",cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JsonIgnore
    private List<OrdersDetail> ordersDetails;
    @Enumerated(EnumType.STRING)
    private PAYMENTMETHOD paymentmethod;
    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;
}

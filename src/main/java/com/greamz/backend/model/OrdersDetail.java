package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greamz.backend.common.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDetail  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    private Integer quantity;
    @ManyToOne
    private Platform platform;
    private Integer discount;
    @ManyToOne
    private GameModel game;
    @ManyToOne
    @JsonBackReference
    private Orders orders;
    @OneToOne
    private Review review;

}

package com.greamz.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
        @ManyToOne(cascade = {CascadeType.MERGE})
        private GameModel game;
        @ManyToOne
        private Orders orders;
    }
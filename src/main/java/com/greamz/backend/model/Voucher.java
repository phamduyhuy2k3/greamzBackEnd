package com.greamz.backend.model;

import com.greamz.backend.common.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Voucher extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String description;
    private Date dateAt;
    private Double dateExpired;
    private Integer discount;
    private Double orderCondition;
    private Double maxPrice;
}

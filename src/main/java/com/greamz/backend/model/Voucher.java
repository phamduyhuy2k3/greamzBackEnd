package com.greamz.backend.model;

import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Voucher extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Date dateAt;
    private Integer dateExpired;
    private Integer discount;
    private Double orderCondition;
    private Double maxPrice;

}

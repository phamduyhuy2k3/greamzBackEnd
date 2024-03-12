package com.greamz.backend.common;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomAuditingEntityListener.class)
public class AbstractAuditEntity {

    @CreationTimestamp
    private ZonedDateTime createdOn;

    @CreatedBy
    private String createdBy;

    @UpdateTimestamp
    private ZonedDateTime lastModifiedOn;

    @LastModifiedBy
    private String lastModifiedBy;
}


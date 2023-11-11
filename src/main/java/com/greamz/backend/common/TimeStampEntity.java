package com.greamz.backend.common;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public class TimeStampEntity {
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreated() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}

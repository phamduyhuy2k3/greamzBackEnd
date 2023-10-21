package com.greamz.backend.common;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@MappedSuperclass
public class TimeStampEntity {
    @Column(updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    private LocalDate updatedAt;

    @PrePersist
    protected void onCreated() {
        createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}

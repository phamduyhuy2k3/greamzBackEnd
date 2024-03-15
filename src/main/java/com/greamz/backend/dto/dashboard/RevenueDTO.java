package com.greamz.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
public class RevenueDTO {
    private Integer month;
    private Double revenue;
    public RevenueDTO(Integer month, Double revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}

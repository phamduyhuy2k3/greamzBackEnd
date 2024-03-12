package com.greamz.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
public class RevenueDTO {
    private String month;
    private Double revenue;
    public RevenueDTO(String month, Double revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}

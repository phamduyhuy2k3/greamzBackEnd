package com.greamz.backend.controller;

import com.greamz.backend.dto.dashboard.RevenueDTO;
import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
import com.greamz.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/getTopSellingProductsInMonthYear")
    public ResponseEntity<Map<String, Object>> getTopSellingProductsInMonthYear(
            @RequestParam int year) {

        return ResponseEntity.ok(dashboardService.getTopSellingProductsInMonthYear(year));
    }

    @GetMapping("/getRevenueByMonth")
    public ResponseEntity<Map<String, Object>> getRevenueByMonth(
            @RequestParam int year) {

        return ResponseEntity.ok(dashboardService.getRevenueByMonth(year));
    }


}

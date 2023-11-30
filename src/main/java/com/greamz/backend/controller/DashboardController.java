package com.greamz.backend.controller;

import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.game.GameDetailClientDTO;
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
    public ResponseEntity<List<TopSellingProductDTO>> getTopSellingProductsInMonthYear(
            @RequestParam int year, @RequestParam(defaultValue = "1") int month) {

        return ResponseEntity.ok(dashboardService.getTopSellingProductsInMonthYear(year, month));
    }

    @GetMapping("/getRevenueByMonth")
    public ResponseEntity<Map<String, Object>> getRevenueByMonth(
            @RequestParam int year) {

        return ResponseEntity.ok(dashboardService.getRevenueByMonth(year));
    }
    @GetMapping("/specialOffer")
    public ResponseEntity<List<GameBasicDTO>> getSpecialOffer() {

        return ResponseEntity.ok(dashboardService.getSpecialOffer());
    }
    @GetMapping("/getTopSellingClient")
    public ResponseEntity<List<GameDetailClientDTO>> getTopSellingClient(
            @RequestParam int year, @RequestParam(defaultValue = "1") int month) {

        return ResponseEntity.ok(dashboardService.getTopSellingClient(year, month));
    }

}

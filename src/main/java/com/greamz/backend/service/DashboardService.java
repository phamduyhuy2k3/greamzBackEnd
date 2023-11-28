package com.greamz.backend.service;

import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
import com.greamz.backend.repository.IGameRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DashboardService {
    private final IGameRepo gameRepo;
    private List<TopSellingProductDTO> mapResultListToDTO(List<Object[]> resultList) {
        // Ánh xạ dữ liệu từ Object[] vào DTO
        // Có thể sử dụng MapStruct hoặc ModelMapper để giảm mã và tăng tính tái sử dụng
        // Trong ví dụ này, chúng ta sử dụng cách thủ công để minh họa
        List<TopSellingProductDTO> dtos = new ArrayList<>();

        for (Object[] result : resultList) {
            TopSellingProductDTO dto = new TopSellingProductDTO();
            dto.setAppid((Long) result[0]);
            dto.setName((String) result[1]);
            dto.setImage((String) result[2]);
            dto.setWebsite((String) result[3]);
            dto.setPrice((Double) result[4]);
            dto.setTotalQuantitySold((BigDecimal) result[5]);
            dtos.add(dto);
        }

        return dtos;
    }
    @Transactional
    public Map<String, Object > getTopSellingProductsInMonthYear(int yearParam) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put("month" + i, mapResultListToDTO(gameRepo.getTopSellingProductsInMonthYear(yearParam, i)));
        }

        return map;
    }
}

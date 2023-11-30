package com.greamz.backend.service;

import com.greamz.backend.dto.dashboard.RevenueDTO;
import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.util.Mapper;
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
    private List<RevenueDTO> mapResultListToRevenueDTO(List<Object[]> resultList) {
        // Ánh xạ dữ liệu từ Object[] vào DTO
        // Có thể sử dụng MapStruct hoặc ModelMapper để giảm mã và tăng tính tái sử dụng
        // Trong ví dụ này, chúng ta sử dụng cách thủ công để minh họa
        List<RevenueDTO> dtos = new ArrayList<>();

        for (Object[] result : resultList) {
            RevenueDTO dto = new RevenueDTO();
            dto.setYear((Long) result[0]);
            dto.setMonth((Long) result[1]);
            dto.setRevenue((Double) result[2]);
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public List<TopSellingProductDTO>getTopSellingProductsInMonthYear(int yearParam, int monthParam) {
        return mapResultListToDTO(gameRepo.getTopSellingProductsInMonthYear(yearParam, monthParam));
    }
    @Transactional
    public List<GameDetailClientDTO>getTopSellingClient(int yearParam, int monthParam) {
        List<Object[]> resultList= gameRepo.getTopSellingProductsInMonthYear(yearParam, monthParam);
        List<GameDetailClientDTO> dtos = new ArrayList<>();

        for (Object[] result : resultList) {
            GameDetailClientDTO dto = new GameDetailClientDTO();
            dto.setAppid((Long) result[0]);
            dto.setName((String) result[1]);
            dto.setHeader_image((String) result[2]);
            dto.setWebsite((String) result[3]);
            dto.setPrice((Double) result[4]);
            dto.setDiscount((Integer) result[6]);
            dtos.add(dto);
        }
        return dtos;
    }
    @Transactional
    public Map<String, Object> getRevenueByMonth(int yearParam) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            map.put("month" + i, mapResultListToRevenueDTO(gameRepo.getRevenueByMonth(yearParam, i)));
        }

        return map;
    }
    public List<GameBasicDTO> getSpecialOffer() {
        List<GameModel> gameModels=gameRepo.specialOffer();
        return gameModels.stream().map(gameModel -> {
            return Mapper.mapObject(gameModel, GameBasicDTO.class);
        }).toList();
    }
}

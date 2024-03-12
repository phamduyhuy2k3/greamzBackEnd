package com.greamz.backend.service;

import com.greamz.backend.dto.dashboard.RevenueDTO;
import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.repository.IOrderRepo;
import com.greamz.backend.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final IOrderRepo orderRepo;



    @Transactional
    public List<TopSellingProductDTO>getTopSellingProductsInMonthYear(int yearParam, int monthParam) {
        return gameRepo.getTopSellingProductsInMonthYear(yearParam, monthParam);
    }
    @Transactional
    public List<GameDetailClientDTO>getTopSellingClient(int yearParam, int monthParam) {
        List<GameModel> resultList= gameRepo.getTopSellingProductsInMonthYearFromClient(yearParam, monthParam);
        List<GameDetailClientDTO> dtos = resultList.stream().map(gameModel -> {
            return Mapper.mapObject(gameModel, GameDetailClientDTO.class);
        }).toList();
        return dtos;
    }
    @Transactional
    public Map<String, Object> getRevenueByMonth(int yearParam) {
        Map<String, Object> map = new HashMap<>();
        gameRepo.getRevenueByMonth(yearParam).stream().forEach(revenueDTO -> {
            map.put(revenueDTO.getMonth().toString(), revenueDTO.getRevenue());
        });
        System.out.println(        map.toString());
        return map;
    }
    @Transactional(readOnly = true)
    public List<GameBasicDTO> getSpecialOffer() {
        List<GameModel> gameModels=gameRepo.specialOffer();
        return gameModels.stream().map(gameModel -> {
            return Mapper.mapObject(gameModel, GameBasicDTO.class);
        }).toList();
    }

}

package com.greamz.backend.service;

import com.greamz.backend.model.GameCategory;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.IGameCategory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameCategoryService {
    @Autowired
    private IGameCategory gameCategoryRepository;
    @Transactional
    public List<GameCategory> findAll() {
        return gameCategoryRepository.findAll();
    }
}

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
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class GameCategoryService {
    private final IGameCategory repo;
    @Transactional
    public List<GameCategory> findAll() {
        return repo.findAll();
    }

    @Transactional
    public GameCategory findById(Long id) throws NoSuchElementException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));
    }

    @Transactional
    public GameCategory saveGameCategory(GameCategory gameCategory) {
        return repo.save(gameCategory);
    }

    @Transactional
    public void deleteGameCategoryById(Long id) {
        GameCategory gameCategory = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));
        repo.deleteById(id);
    }
}

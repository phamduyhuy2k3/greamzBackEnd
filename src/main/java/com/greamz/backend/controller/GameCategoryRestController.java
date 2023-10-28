package com.greamz.backend.controller;

import com.greamz.backend.model.GameCategory;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.GameCategoryService;
import com.greamz.backend.service.GameModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class GameCategoryRestController {
    private final GameCategoryService service;
    @GetMapping("/findAll")
    public ResponseEntity<List<GameCategory>> findAll(){
        List<GameCategory> gameCategories = service.findAll();
        return ResponseEntity.ok(gameCategories);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<GameCategory> findById(@PathVariable("id") Long id){
        try {
            GameCategory gameCategory = service.findById(id);
            return ResponseEntity.ok(gameCategory);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public GameCategory getOne(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PostMapping("/save")
    public GameCategory save(@RequestBody GameCategory gameCategory){
        service.saveGameCategory(gameCategory);
        return gameCategory;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id){
        service.deleteGameCategoryById(id);
    }
    @GetMapping("/game/{id}")
    public ResponseEntity<List<GameCategory>> findByGameId(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findByGameModelsAppid(id));
    }
}

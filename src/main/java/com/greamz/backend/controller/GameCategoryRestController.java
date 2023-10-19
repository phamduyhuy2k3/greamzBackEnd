package com.greamz.backend.controller;

import com.greamz.backend.model.GameCategory;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.GameCategoryService;
import com.greamz.backend.service.GameModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class GameCategoryRestController {
    private final GameCategoryService service;
    @GetMapping("/findALl")
    public ResponseEntity<List<GameCategory>> findAll(){
        List<GameCategory> gameCategories = service.findAll();
        return ResponseEntity.ok(gameCategories);
    }
}

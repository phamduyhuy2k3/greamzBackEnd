package com.greamz.backend.controller;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.GameCategory;
import com.greamz.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<List<GameCategory>> finAdll(){

        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<Set<GameCategory>> finAdllByType(@PathVariable CategoryTypes type){

        return ResponseEntity.ok(categoryService.getCategoryByType(type));
    }
    @GetMapping("/types")
    public ResponseEntity<List<String>> findCategoryByType(){

        return ResponseEntity.ok(Arrays.stream(CategoryTypes.values()).map(CategoryTypes::name).toList());
    }
}

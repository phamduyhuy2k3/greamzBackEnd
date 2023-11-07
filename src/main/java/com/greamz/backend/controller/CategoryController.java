package com.greamz.backend.controller;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/type/{type}")
    public ResponseEntity<Set<Category>> finAdllByType(@PathVariable CategoryTypes type) {

        return ResponseEntity.ok(service.findAllByCategoryTypes(type));
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> findCategoryByType() {

        return ResponseEntity.ok(Arrays.stream(CategoryTypes.values()).map(CategoryTypes::name).toList());
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Category>> findAll() {
        List<Category> gameCategories = service.findAll();
        return ResponseEntity.ok(gameCategories);
    }
    @GetMapping("/search")
    public ResponseEntity<Iterable<Category>> searchGame(@RequestParam(defaultValue = "") String term,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "7") int size) {
        Iterable<Category> categoryModels = service.searchCategory(term, page, size);
        return ResponseEntity.ok(categoryModels);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<Category> findById(@PathVariable("id") Long id) {
        try {
            Category category = service.findById(id);
            return ResponseEntity.ok(category);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("{id}")
    public Category getOne(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/save")
    public Category save(@RequestBody Category category) {
        service.saveGameCategory(category);
        return category;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.deleteGameCategoryById(id);
    }
    @GetMapping("/findAllPagination")
    public ResponseEntity<?> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }

}

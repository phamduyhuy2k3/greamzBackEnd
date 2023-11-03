package com.greamz.backend.service;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import com.greamz.backend.repository.ICategory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {
    private final ICategory repo;
    @Transactional
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Category findById(Long id) throws NoSuchElementException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));
    }

    @Transactional
    public Category saveGameCategory(Category category) {
        return repo.save(category);
    }

    @Transactional
    public void deleteGameCategoryById(Long id) {
        repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));
        repo.deleteById(id);
    }

    public Set<Category> findAllByCategoryTypes(CategoryTypes categoryTypes) {
        return repo.findAllByCategoryTypes(categoryTypes);
    }
    public List<Category> findAllByGameModelsAppid(Long gameId) {
        return repo.findAllByGameModelsAppid(gameId);
    }
    @Transactional
    public Page<Category> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }

}

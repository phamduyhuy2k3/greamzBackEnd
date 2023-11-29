package com.greamz.backend.service;

import com.greamz.backend.dto.category.CategoryBasicDTO;
import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.ICategoryRepo;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {
    private final ICategoryRepo repo;
    private final IGameRepo gameRepo;
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        List<Category> gameCategories = repo.findAll();
        for (Category category : gameCategories) {
            // Initialize the collection to avoid LazyInitializationException
            Hibernate.initialize(category.getGameModels());

            // Count the number of games
            long gameCount = category.getGameModels().size();
            System.out.println(gameCount);

            // Set the game count in the category
            category.setGameCount(gameCount);

            // Set the gameModels to null to prevent serialization issues
            category.setGameModels(null);
        }
        return gameCategories;
    }
    @Transactional(readOnly = true)
    public List<CategoryBasicDTO> findAllFromClient() {
        List<Category> gameCategories = repo.findAll();
        List<CategoryBasicDTO> categoryBasicDTOS = gameCategories.stream().map(
                category -> {
                    CategoryBasicDTO categoryBasicDTO = Mapper.mapObject(category, CategoryBasicDTO.class);
                    categoryBasicDTO.setGameCount(gameRepo.countAllByCategoriesId(category.getId()));
                    return categoryBasicDTO;
                })
                .toList();
        return categoryBasicDTOS;
    }
    @Transactional(readOnly = true)
    public Category findById(Long id) throws NoSuchElementException {
        Category category=repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));

        return category;
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
    @Transactional(readOnly = true)
    public Set<Category> findAllByCategoryTypes(CategoryTypes categoryTypes) {
        return repo.findAllByCategoryTypes(categoryTypes);
    }
    @Transactional(readOnly = true)
    public List<Category> findAllByGameModelsAppid(Long gameId) {
        return repo.findAllByGameModelsAppid(gameId);
    }
    @Transactional(readOnly = true)
    public Page<Category> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> gameCategories = repo.findAll(pageable);
        gameCategories.forEach(category -> category.setGameModels(null));

        return gameCategories;
    }
    @Transactional(readOnly = true)
    public Page<Category> searchCategory(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Category> categoryModelPage = repo.searchCategory(searchTerm, pageable);
        categoryModelPage.forEach(gameModel -> {
            Hibernate.initialize(gameModel.getGameModels());

        });

        return categoryModelPage;
    }

}

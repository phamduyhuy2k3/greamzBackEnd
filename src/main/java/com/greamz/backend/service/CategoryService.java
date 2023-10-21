package com.greamz.backend.service;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.GameCategory;
import com.greamz.backend.repository.ICategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepo categoryRepository;
    public void addCategory(GameCategory name){
        GameCategory gameCategory= categoryRepository.saveAndFlush(name);
    }
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
    public void updateCategory(GameCategory name){
        GameCategory gameCategory= categoryRepository.saveAndFlush(name);
    }
    public GameCategory getCategory(Long id){
        return categoryRepository.findById(id).get();
    }
    public List<GameCategory> getAllCategory(){
        return categoryRepository.findAll();
    }
    public Set<GameCategory> getCategoryByType(CategoryTypes type){
        return categoryRepository.findAllByCategoryTypes(type);
    }

}

package com.greamz.backend.service;

import com.greamz.backend.common.BaseEntityService;
import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.GameCategory;
import com.greamz.backend.repository.ICategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service

public class CategoryService extends BaseEntityService<GameCategory,Long,ICategoryRepo> {
    public CategoryService(ICategoryRepo repository) {
        super(repository);
    }

    @Override
    public List<GameCategory> getAll() {
        return super.getAll();
    }

    public GameCategory findById(Long id){
        return super.getById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
    public Set<GameCategory> getCategoryByType(CategoryTypes type){
        return repository.findAllByCategoryTypes(type);
    }
}

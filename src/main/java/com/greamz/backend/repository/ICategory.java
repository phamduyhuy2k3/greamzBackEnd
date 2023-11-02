package com.greamz.backend.repository;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ICategory extends JpaRepository<Category, Long> {

    Set<Category> findAllByCategoryTypes(CategoryTypes categoryTypes);
//    Page<Category> searchProduct(String search, Pageable pageable);

}

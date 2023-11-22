package com.greamz.backend.controller;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import com.greamz.backend.service.CategoryService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testFindAllByType() {
//        // Test for finAdllByType method
//        CategoryTypes type = CategoryTypes.GENRES; // Replace with actual category type
//
//        Set<Category> mockCategories = Set.of(new Category(/*mock parameters*/), new Category(/*mock parameters*/));
//        when(categoryService.findAllByCategoryTypes(type)).thenReturn(mockCategories);
//
//        ResponseEntity<Set<Category>> response = categoryController.finAdllByType(type);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        // Add more assertions as needed
//    }
//
//    @Test
//    public void testFindCategoryByType() {
//        // Test for findCategoryByType method
//        List<String> mockCategoryTypes = Arrays.asList("Type1", "Type2", "Type3");
//        when(categoryService.findAllByCategoryTypes()).thenReturn(mockCategoryTypes);
//
//        ResponseEntity<List<String>> response = categoryController.findCategoryByType();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        // Add more assertions as needed
//
//    }

//    @Test
//    public void testSearchGame() {
//        // Test for searchGame method
//        String term = "searchTerm"; // Replace with actual search term
//        int page = 1;
//        int size = 5;
//
//        List<Category> mockCategories = Arrays.asList(new Category(/*mock parameters*/), new Category(/*mock parameters*/));
//        when(categoryService.searchCategory(term, page, size)).thenReturn((Page<Category>) mockCategories);
//
//        ResponseEntity<Iterable<Category>> response = categoryController.searchGame(term, page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        // Add more assertions as needed
//    }

    @Test
    public void testFindByIdWithValidId() {
        // Test for findById method with a valid ID
        Long categoryId = 1L; // Replace with actual category ID
        when(categoryService.findById(categoryId)).thenReturn(new Category(/*mock parameters*/));

        ResponseEntity<Category> response = categoryController.findById(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testFindByIdWithInvalidId() {
        // Test for findById method with an invalid ID
        Long categoryId = 999L; // Assuming this ID does not exist
        when(categoryService.findById(categoryId)).thenThrow(NoSuchElementException.class);

        ResponseEntity<Category> response = categoryController.findById(categoryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetOne() {
        // Test for getOne method
        Long categoryId = 1L; // Replace with actual category ID
        Category mockCategory = new Category(/*mock parameters*/);
        when(categoryService.findById(categoryId)).thenReturn(mockCategory);

        Category response = categoryController.getOne(categoryId);

        assertNotNull(response);
        // Add more assertions as needed
    }

    @Test
    public void testSave() {
        // Test for save method
        Category mockCategory = new Category(/*mock parameters*/);
        when(categoryService.saveGameCategory(mockCategory)).thenReturn(mockCategory);

        Category response = categoryController.save(mockCategory);

        assertNotNull(response);
        // Add more assertions as needed
    }

    @Test
    public void testDelete() {
        // Test for delete method
        Long categoryId = 1L; // Replace with actual category ID

        categoryController.delete(categoryId);
        // Add assertions as needed
    }

    @Test
    public void testFindAllPagination() {
        // Test for findAllPagination method
        int page = 1;
        int size = 5;

        List<Category> mockCategoriesList = Arrays.asList(new Category(/*mock parameters*/), new Category(/*mock parameters*/));
        Page<Category> mockCategoriesPage = new PageImpl<>(mockCategoriesList);
        when(categoryService.findAll(page, size)).thenReturn(mockCategoriesPage);

        ResponseEntity<?> response = categoryController.findAllPagination(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }
}

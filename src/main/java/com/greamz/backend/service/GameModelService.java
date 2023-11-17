package com.greamz.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.dto.GameFilter;
import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Platform;
import com.greamz.backend.repository.IGameRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.boot.model.source.spi.Sortable;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class GameModelService {
    @PersistenceContext
    private EntityManager entityManager;
    private final IGameRepo gameModelRepository;
    private final CategoryService categoryService;

    @Transactional
    public GameModel saveGameModel(GameModel gameModel) {
        return gameModelRepository.saveAndFlush(gameModel);
    }
//    @Transactional(readOnly = true)
//    public List<GameModel> findAll(Long id) {
//        List<GameModel> gameModelByCategory = gameModelRepository.findAllByCategoriesId(id);
//        gameModelByCategory.forEach(gameModel -> {
//            gameModel.setReviews(null);
//            gameModel.setSupported_languages(null);
//            gameModel.setMovies(null);
//            gameModel.setImages(null);
//            Hibernate.initialize(gameModel.getCategories());
//            Hibernate.initialize(gameModel.getPlatform());
//        });
//        return gameModelByCategory;
//    }

    @Transactional(readOnly = true)
    public Page<GameModel> findAll(Pageable pageable) {

        Page<GameModel> gameModelPage = gameModelRepository.findAll(pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setReviews(null);
            gameModel.setSupported_languages(null);
            gameModel.setMovies(null);
            gameModel.setImages(null);
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getPlatform());
            Hibernate.initialize(gameModel.getReviews());
        });
        return gameModelPage;
    }

    @Transactional(readOnly = true)
    public GameModel findGameByAppid(Long appid) throws NoSuchElementException {
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
        List<Category> categories = categoryService.findAllByGameModelsAppid(appid);
        gameModel.setCategories(categories);
        gameModel.setReviews(null);
        Hibernate.initialize(gameModel.getImages());
        Hibernate.initialize(gameModel.getMovies());
        Hibernate.initialize(gameModel.getSupported_languages());
        Hibernate.initialize(gameModel.getCategories());
        Hibernate.initialize(gameModel.getPlatform());
        return gameModel;
    }

    @Transactional(readOnly = true)
    public List<GameModel> findGameByCategory(Long categoryId) {
        List<GameModel> gameModelByCategory = gameModelRepository.findAllByCategoriesId(categoryId);
        gameModelByCategory.forEach(gameModel -> {
            gameModel.setReviews(null);
            gameModel.setSupported_languages(null);
            gameModel.setMovies(null);
            gameModel.setImages(null);
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getPlatform());
        });
        return gameModelByCategory;
    }


    @Transactional(readOnly = true)
    public List<GameModel> findGameByGameIds(String ids) {
        List<Long> idList = parseIds(ids);
        System.out.println(idList);
        List<GameModel> gameModels=gameModelRepository.findAllById(idList);
        System.out.println(gameModels.size());
        gameModels.forEach(gameModel -> {
            gameModel.setImages(null);
            gameModel.setMovies(null);
            gameModel.setSupported_languages(null);
            gameModel.setReviews(null);
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getPlatform());
        });
        return gameModels;
    }
    @Transactional
    public void deleteGameByAppid(Long appid) {
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
        gameModelRepository.delete(gameModel);
    }
    @Transactional(readOnly = true)
    public Page<GameModel> searchGame(String searchTerm,Pageable pageable) {
        Page<GameModel> gameModelPage = gameModelRepository.searchGame(searchTerm, pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setSupported_languages(null);
            gameModel.setReviews(null);
            Hibernate.initialize(gameModel.getCategories());
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setPlatform(null);
        });

        return gameModelPage;
    }
    @Transactional(readOnly = true)
    public Page<GameModel> filterGamesByCategoriesAndPlatform(
            String q,
            String categoriesId,
            Long platformId,
            int page,
            int size,
            String devices,
            Double minPrice,
            Double maxPrice,
            String sort,
            Sort.Direction direction
    ) {
        List<Specification<GameModel>> gameModelSpecifications = new ArrayList<>();
         if(categoriesId.isBlank() && platformId==-1 && devices.isBlank() && minPrice==-1 && maxPrice==-1 &&q.isBlank() &&!sort.isBlank()){
            Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(direction,sort));
            return findAll(pageable);
        }else if(categoriesId.isBlank() && platformId==-1 && devices.isBlank() && minPrice==-1 && maxPrice==-1 &&q.isBlank()){
             Pageable pageable = PageRequest.of(page, size);
             return findAll(pageable);
         }
        if(!categoriesId.isBlank()){
            Specification<GameModel> categorySpecification = (root, query, criteriaBuilder) -> {
                Join<GameModel, Category> categoryJoin = root.join("categories", JoinType.INNER);
                return categoryJoin.get("id").in(parseIds(categoriesId));
            };
            gameModelSpecifications.add(categorySpecification);
        }
        if(platformId!=-1){
            Specification<GameModel> platformSpecification = (root, query, criteriaBuilder) -> {
                Predicate platformPredicate = criteriaBuilder.equal(root.get("platform").get("id"), platformId);
                return platformPredicate;
            };
            gameModelSpecifications.add(platformSpecification);
        }
        if(!devices.isBlank()){
            Specification<GameModel> devicesSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("devices"), Devices.valueOf(devices));;
            gameModelSpecifications.add(devicesSpecification);
        }
        if (!q.isBlank()) {
            Specification<GameModel> searchSpecification = (root, query, criteriaBuilder) -> {
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("detailed_description")), "%" + q.toLowerCase() + "%");
                Predicate shortDescriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("short_description")), "%" + q.toLowerCase() + "%");
                Predicate categoryNamePredicate = criteriaBuilder.in(root.join("categories", JoinType.INNER).get("name"))
                        .value(q.toLowerCase());
                Predicate pricePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(criteriaBuilder.function("CAST", String.class, root.get("price"))),
                        "%" + q.toLowerCase() + "%");
                return criteriaBuilder.or(
                        namePredicate,
                        descriptionPredicate,
                        shortDescriptionPredicate,
                        categoryNamePredicate,
                        pricePredicate
                );
            };
            gameModelSpecifications.add(searchSpecification);
        }
        if(minPrice!=-1 || maxPrice!=-1){
            Specification<GameModel> priceSpecification = (root, query, criteriaBuilder) -> {
                if (minPrice != null && maxPrice != null) {
                    return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
                } else if (minPrice != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
                } else if (maxPrice != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("price"),  maxPrice);
                }
                return null;
            };
            gameModelSpecifications.add(priceSpecification);
        }
        Specification<GameModel> combinedSpecification= Specification.anyOf(gameModelSpecifications);

        Pageable pageable;
        if(sort==null ||sort.isBlank()){
            pageable = PageRequest.of(page, size);
        }else {
            pageable=PageRequest.of(page, size).withSort(Sort.by(direction,sort));
        }

        Page<GameModel> gameModelPage = gameModelRepository.findAll(combinedSpecification, pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setCategories(null);
            gameModel.setImages(null);
            gameModel.setReviews(null);
            gameModel.setMovies(null);
            gameModel.setSupported_languages(null);
            gameModel.setPlatform(null);
        });
        return gameModelPage;
    }

    public List<Category> getCategoriesForGame(GameModel gameModel) {
        // Load the categories when needed
        return gameModel.getCategories();
    }

    public List<String> getSupportedLanguagesForGame(GameModel gameModel) {
        // Load the supported_languages when needed
        return gameModel.getSupported_languages();
    }


    private List<Long> parseIds(String idsParam) {
        if (idsParam == null || idsParam.equals("null") || idsParam.isBlank()) {
            return Collections.emptyList();
        } else {
            List<String> idStrings = Arrays.asList(idsParam.split(","));
            return idStrings.stream()
                    .filter(s -> !s.isEmpty()) // Filter out empty strings
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
    }
}

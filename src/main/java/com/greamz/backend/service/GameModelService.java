package com.greamz.backend.service;

import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.platform.PlatformDTO;
import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.dto.game.GenreDTO;
import com.greamz.backend.model.*;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.repository.IReviewRepo;
import com.greamz.backend.util.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CodeActiveService codeActiveService;
    private final IReviewRepo reviewRepo;
    @Transactional
    public GameModel saveGameModel(GameModel gameModel) {
        return gameModelRepository.saveAndFlush(gameModel);
    }

    @Transactional(rollbackFor = NoSuchElementException.class)
    public void updateStockForGameFromOrder(List<OrdersDetail> ordersDetail) {
        ordersDetail.forEach(ordersDetail1 -> {
           List<CodeActive> codeActiveList= codeActiveService.findByIdGameAndPlatformNotActiveAndAccountnull(ordersDetail1.getGame().getAppid(), ordersDetail1.getPlatform().getId());
            if(ordersDetail1.getQuantity()<=codeActiveList.size()){
                codeActiveList.stream().limit(ordersDetail1.getQuantity()).forEach(codeActive -> {
                    codeActive.setAccount(ordersDetail1.getOrders().getAccount());
                    codeActiveService.save(codeActive);
                });
            }
        });


    }

    @Transactional(readOnly = true)
    public Page<GameModel> findAll(Pageable pageable) {

        Page<GameModel> gameModelPage = gameModelRepository.findAll(pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setReviews(null);
            gameModel.setSupported_languages(null);
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setCodeActives(null);
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getPlatforms());
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
        gameModel.setCodeActives(null);
        gameModel.setPlatforms(null);
        Hibernate.initialize(gameModel.getImages());
        Hibernate.initialize(gameModel.getMovies());
        Hibernate.initialize(gameModel.getSupported_languages());
        Hibernate.initialize(gameModel.getCategories());
        return gameModel;
    }
    @Transactional(readOnly = true)
    public GameDetailClientDTO findGameByIdFromClientRequest(Long appid){
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
        Hibernate.initialize(gameModel.getImages());
        Hibernate.initialize(gameModel.getMovies());
        Hibernate.initialize(gameModel.getSupported_languages());

        Hibernate.initialize(gameModel.getCategories());
        List<PlatformDTO> platforms =codeActiveService.findAllPlatform(appid);

        return GameDetailClientDTO
                .builder()
                .appid(gameModel.getAppid())
                .name(gameModel.getName())
                .averageRating(reviewRepo.calculateAverageRating(gameModel.getAppid()))
                .totalReviewed(reviewRepo.countAllByGameAppid(gameModel.getAppid()))
                .detailed_description(gameModel.getDetailed_description())
                .about_the_game(gameModel.getAbout_the_game())
                .short_description(gameModel.getShort_description())
                .header_image(gameModel.getHeader_image())
                .website(gameModel.getWebsite())
                .capsule_image(gameModel.getCapsule_image())
                .images(gameModel.getImages())
                .discount(gameModel.getDiscount())
                .movies(gameModel.getMovies())
                .price(gameModel.getPrice())
                .platforms(platforms)
                .categories(gameModel.getCategories().stream().map(category -> new GenreDTO(category.getId(), category.getName())).collect(Collectors.toList()))
                .build();
    }


    @Transactional(readOnly = true)
    public List<GameModel> findGameByCategory(Long categoryId) {
        List<GameModel> gameModelByCategory = gameModelRepository.findAllByCategoriesId(categoryId);
        gameModelByCategory.forEach(gameModel -> {
            gameModel.setReviews(null);
            gameModel.setSupported_languages(null);
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setCodeActives(null);
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getPlatforms());
        });
        return gameModelByCategory;
    }


    @Transactional(readOnly = true)
    public List<GameDetailClientDTO> findGameByGameIds(String ids,String platformIds) {
        List<Long> idList = parseIds(ids);
        List<Integer> platformIdList = parseIdsInt(platformIds);
        List<GameModel> gameModels = gameModelRepository.findAllByAppidsAndPlatformIds(idList,platformIdList);
        return gameModels.stream().map(gameModel -> {
            List<PlatformDTO> platforms =codeActiveService.findAllPlatform(gameModel.getAppid());
           return GameDetailClientDTO
                    .builder()
                    .appid(gameModel.getAppid())
                    .name(gameModel.getName())
                    .header_image(gameModel.getHeader_image())
                    .discount(gameModel.getDiscount())
                    .price(gameModel.getPrice())
                    .platforms(platforms)
                    .build();
        }).toList();

    }

    @Transactional
    public void deleteGameByAppid(Long appid) {
        Query deleteQuery = entityManager.createNativeQuery("DELETE FROM game_category WHERE game_id= :gameId");
        deleteQuery.setParameter("gameId", appid);
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Deleted " + deletedRows + " game_category records.");
        gameModelRepository.deleteById(appid);
    }
    @Transactional(readOnly = true)
    public Page<GameModel> searchGame(String searchTerm, Pageable pageable) {
        Page<GameModel> gameModelPage = gameModelRepository.searchGame(searchTerm, pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setSupported_languages(null);
            gameModel.setReviews(null);
            Hibernate.initialize(gameModel.getCategories());
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setPlatforms(null);
            gameModel.setCodeActives(null);
        });

        return gameModelPage;
    }

//    @Transactional(readOnly = true)
//    public Page<GameDetailClientDTO> searchGame(String searchTerm, Pageable pageable) {
//        Page<GameModel> gameModelPage = gameModelRepository.searchGame(searchTerm, pageable);
//        Page<GameDetailClientDTO> gameDetailClientDTOPage = gameModelPage.map(gameModel -> {
//            Hibernate.initialize(gameModel.getImages());
//            Hibernate.initialize(gameModel.getMovies());
//            Hibernate.initialize(gameModel.getSupported_languages());
//            Hibernate.initialize(gameModel.getCategories());
//            List<PlatformDTO> platforms =codeActiveService.findAllPlatform(gameModel.getAppid());
//            return GameDetailClientDTO
//                    .builder()
//                    .appid(gameModel.getAppid())
//                    .name(gameModel.getName())
//                    .detailed_description(gameModel.getDetailed_description())
//                    .about_the_game(gameModel.getAbout_the_game())
//                    .short_description(gameModel.getShort_description())
//                    .header_image(gameModel.getHeader_image())
//                    .website(gameModel.getWebsite())
//                    .capsule_image(gameModel.getCapsule_image())
//                    .images(gameModel.getImages())
//                    .movies(gameModel.getMovies())
//                    .price(gameModel.getPrice())
//                    .platforms(platforms)
//                    .categories(gameModel.getCategories().stream().map(category -> new GenreDTO(category.getId(), category.getName())).collect(Collectors.toList()))
//                    .build();
//        });
//
//        return gameDetailClientDTOPage;
//    }
    @Transactional(readOnly = true)
    public Page<GameModel> searchGameByName(String searchTerm, Pageable pageable) {
        Page<GameModel> gameModelPage = gameModelRepository.searchGameByName(searchTerm, pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setSupported_languages(null);
            gameModel.setReviews(null);
            Hibernate.initialize(gameModel.getCategories());
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setPlatforms(null);
            gameModel.setCodeActives(null);
        });

        return gameModelPage;
    }
    @Transactional(readOnly = true)
    public Page<GameModel> searchGameByCategory(String searchTerm, Pageable pageable) {
        Page<GameModel> gameModelPage = gameModelRepository.searchGameByCategory(searchTerm, pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setSupported_languages(null);
            gameModel.setReviews(null);
            Hibernate.initialize(gameModel.getCategories());
            gameModel.setMovies(null);
            gameModel.setImages(null);
            gameModel.setPlatforms(null);
            gameModel.setCodeActives(null);
        });

        return gameModelPage;
    }

    @Transactional(readOnly = true)
    public Page<GameDetailClientDTO> filterGamesByCategoriesAndPlatform(
            String q,
            String categoriesId,

            int page,
            int size,
//            String devices,
            Double minPrice,
            Double maxPrice,
            String sort,
            Sort.Direction direction
    ) {
        List<Specification<GameModel>> gameModelSpecifications = new ArrayList<>();
        if (categoriesId.isBlank()  && minPrice == -1 && maxPrice == -1 && q.isBlank() && !sort.isBlank()) {
            Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(direction, sort));
            return findAll(pageable).map(gameModel -> GameDetailClientDTO
                    .builder()
                    .appid(gameModel.getAppid())
                    .name(gameModel.getName())
                    .header_image(gameModel.getHeader_image())
                    .price(gameModel.getPrice())
                    .build());
        } else if (categoriesId.isBlank()  && minPrice == -1 && maxPrice == -1 && q.isBlank()) {
            Pageable pageable = PageRequest.of(page, size);
            return findAll(pageable).map(gameModel -> GameDetailClientDTO
                    .builder()
                    .appid(gameModel.getAppid())
                    .name(gameModel.getName())
                    .header_image(gameModel.getHeader_image())
                    .price(gameModel.getPrice())
                    .build());
        }
        if (!categoriesId.isBlank()) {
            Specification<GameModel> categorySpecification = (root, query, criteriaBuilder) -> {
                Join<GameModel, Category> categoryJoin = root.join("categories", JoinType.INNER);
                return categoryJoin.get("id").in(parseIds(categoriesId));
            };
            gameModelSpecifications.add(categorySpecification);
        }
//        if (platformId != -1) {
//            Specification<GameModel> platformSpecification = (root, query, criteriaBuilder) -> {
//                Predicate platformPredicate = criteriaBuilder.equal(root.get("platform").get("id"), platformId);
//                return platformPredicate;
//            };
//            gameModelSpecifications.add(platformSpecification);
//        }

        if (!q.isBlank()) {
            Specification<GameModel> searchSpecification = (root, query, criteriaBuilder) -> {
                Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("detailed_description")), "%" + q.toLowerCase() + "%");
                Predicate shortDescriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("short_description")), "%" + q.toLowerCase() + "%");
//                Predicate categoryNamePredicate = criteriaBuilder.in(root.join("categories", JoinType.INNER).get("name"))
//                        .value(q.toLowerCase());
                return criteriaBuilder.or(
                        namePredicate,
                        descriptionPredicate,
                        shortDescriptionPredicate
//                        categoryNamePredicate
                );
            };
            gameModelSpecifications.add(searchSpecification);

        }
        Specification<GameModel> priceSpecification = null;
        if (minPrice > 0 && maxPrice > 0 && minPrice < maxPrice || (minPrice == 0 && maxPrice > 0)) {
            priceSpecification = (root, query, criteriaBuilder) -> {
                if (minPrice > 0 && maxPrice > 0) {
                    return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
                } else if (minPrice > 0) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
                } else if (maxPrice > 0) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
                }
                return null;
            };

        }
        Specification<GameModel> combinedSpecification =
                priceSpecification == null ? Specification.anyOf(gameModelSpecifications) :
                        Specification.anyOf(gameModelSpecifications).and(priceSpecification);

        Pageable pageable;
        if (sort == null || sort.isBlank()) {
            pageable = PageRequest.of(page, size);
        } else {
            System.out.println(direction);
            pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        }

        Page<GameModel> gameModelPage = gameModelRepository.findAll(combinedSpecification, pageable);
        Page<GameDetailClientDTO> gameDetailClientDTOPage = gameModelPage.map(gameModel -> GameDetailClientDTO
                .builder()
                .appid(gameModel.getAppid())
                .name(gameModel.getName())
                .header_image(gameModel.getHeader_image())
                .price(gameModel.getPrice())
                .build());
        return gameDetailClientDTOPage;
    }
    public List<Category> getCategoriesForGame(GameModel gameModel) {
        // Load the categories when needed
        return gameModel.getCategories();
    }

    public List<String> getSupportedLanguagesForGame(GameModel gameModel) {
        // Load the supported_languages when needed
        return gameModel.getSupported_languages();
    }

    private List<Long>  parseIds(String idsParam) {
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

    @Transactional(readOnly = true)
    public Long countGamesAddedLastWeek() {
        return gameModelRepository.countGamesAddedLastWeek();
    }
    @Transactional(readOnly = true)
    public Long countAllByAppid() {
        return gameModelRepository.countTotalGames();
    }

    @Transactional(readOnly = true)
    public List<GameBasicDTO> findGameSimialr(String categoryIds, String platformIds) {
        List<GameModel> gameModelByCategory = gameModelRepository.gameSimilar(parseIds(categoryIds), parseIds(platformIds));
        List<GameBasicDTO> gameBasicDTOS=gameModelByCategory.stream().map(gameModel -> Mapper.mapObject(gameModel,GameBasicDTO.class)).collect(Collectors.toList());
        return gameBasicDTOS;
    }
    private List<Integer>  parseIdsInt(String idsParam) {
        if (idsParam == null || idsParam.equals("null") || idsParam.isBlank()) {
            return Collections.emptyList();
        } else {
            List<String> idStrings = Arrays.asList(idsParam.split(","));
            return idStrings.stream()
                    .filter(s -> !s.isEmpty()) // Filter out empty strings
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
    }
}

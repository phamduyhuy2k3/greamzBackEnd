package com.greamz.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.IGameRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;


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

//    private record SteamRequest(String appid) {
//    }
//    public void saveGameModelsInBatch(List<GameModel> gameModels) {
//        int batchSize = 1000;
//        for (int i = 0; i < gameModels.size(); i += batchSize) {
//            List<GameModel> batch = gameModels.subList(i, i + batchSize);
//            gameModelRepository.saveAll(batch);
//            gameModelRepository.flush();
//        }
//    }
//
//    @Async
//    public CompletableFuture<Void> updateGameModels(List<GameModel> gameModels) {
//        for (GameModel gameModel : gameModels) {
//            try {
//                URL url = new URL("http://store.steampowered.com/api/appdetails?appids=" + gameModel.getAppid());
//                System.out.println(gameModel.getAppid());
//                String response = restTemplate.getForObject(url.toURI(), String.class);
//                JsonNode jsonNode = objectMapper.readTree(response);
//                JsonNode dataNode = jsonNode.get(String.valueOf(gameModel.getAppid())).get("data");
//                GameModel updatedData = objectMapper.readValue(dataNode.toString(), GameModel.class);
//                // Update gameModel with the updatedData or apply your logic here
//                updatedData.setAppid(gameModel.getAppid());
//                {
//                    if (updatedData.getScreenshots() != null) {
//                        updatedData.getScreenshots().forEach(screenshot -> {
//                            screenshot.setGameModel(updatedData);
//                        });
//                    }
//                    if (updatedData.getMovies() != null) {
//                        updatedData.getMovies().forEach(movie -> {
//                            movie.setGameModel(updatedData);
//                        });
//                    }
//
//                    gameModelRepository.saveAndFlush(updatedData);
//
//                    log.info("Updated gameModel with appid: {}", gameModel.getAppid());
//                }
//                ;
//            } catch (NullPointerException e) {
//                continue;
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            } catch (JsonMappingException e) {
//                throw new RuntimeException(e);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return CompletableFuture.completedFuture(null);
//    }
//
//    @Transactional
//    public void getGameDetailAndUpdateIt() {
//        List<GameModel> gameModels = gameModelRepository.findAll();
//        int batchSize = 100;
//        int totalModels = gameModels.size();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the thread pool size as needed
//
//        for (int i = 0; i < totalModels; i += batchSize) {
//            List<GameModel> batch = gameModels.subList(i, Math.min(i + batchSize, totalModels));
//            CompletableFuture<Void> future = updateGameModels(batch);
//            // Wait for the batch update to complete
//            future.join();
//        }
//
//        executorService.shutdown();
//    }

    //    @Transactional
//    public List<GameModel> findAll() {
//        return gameModelRepository.findAll();
//    }


    @Transactional(readOnly = true)
    public Page<GameModel> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GameModel> gameModelPage = gameModelRepository.findAll(pageable);
        gameModelPage.forEach(gameModel -> {
            gameModel.setComments(null);
            gameModel.setSupported_languages(null);
            gameModel.setMovies(null);
            gameModel.setImages(null);
            Hibernate.initialize(gameModel.getCategories());

        });
        return gameModelPage;
    }

    @Transactional(readOnly = true)
    public GameModel findGameByAppid(Long appid) throws NoSuchElementException {
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
        List<Category> categories = categoryService.findAllByGameModelsAppid(appid);
        gameModel.setCategories(categories);
        Hibernate.initialize(gameModel.getImages());
        Hibernate.initialize(gameModel.getMovies());
        Hibernate.initialize(gameModel.getSupported_languages());
        return gameModel;
    }

    @Transactional(readOnly = true)
    public List<GameModel> findGameByCategory(Long categoryId) {
        return gameModelRepository.findAllByCategoriesId(categoryId);
    }

    @Transactional
    public void deleteGameByAppid(Long appid) {
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
        gameModelRepository.delete(gameModel);

    }

    @Transactional(readOnly = true)
    public Page<GameModel> searchGame(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<GameModel> gameModelPage = gameModelRepository.searchGame(searchTerm, pageable);
        gameModelPage.forEach(gameModel -> {
            Hibernate.initialize(gameModel.getImages());
            Hibernate.initialize(gameModel.getMovies());
            Hibernate.initialize(gameModel.getCategories());
            Hibernate.initialize(gameModel.getSupported_languages());
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
//    @Transactional
//    public List<GameModel> findGameByCategory(Long categoryId) {
//        return gameModelRepository.findByGameCategory_Id(categoryId);
//    }

}

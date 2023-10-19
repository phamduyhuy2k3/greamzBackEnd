package com.greamz.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.repository.IGameRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class GameModelService {
    private final RestTemplate restTemplate;
    private final IGameRepo gameModelRepository;
    private final MovieService movieService;
    private final ScreenShotService screenShotService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveGameModel(GameModel gameModel) {
        gameModelRepository.save(gameModel);
    }

    private record SteamRequest(String appid) {
    }

    public void saveGameModelsInBatch(List<GameModel> gameModels) {
        int batchSize = 1000;
        for (int i = 0; i < gameModels.size(); i += batchSize) {
            List<GameModel> batch = gameModels.subList(i, i + batchSize);
            gameModelRepository.saveAll(batch);
            gameModelRepository.flush();
        }
    }

    @Async
    public CompletableFuture<Void> updateGameModels(List<GameModel> gameModels) {
        for (GameModel gameModel : gameModels) {
            try {
                URL url = new URL("http://store.steampowered.com/api/appdetails?appids=" + gameModel.getAppid());
                System.out.println(gameModel.getAppid());
                String response = restTemplate.getForObject(url.toURI(), String.class);
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode dataNode = jsonNode.get(String.valueOf(gameModel.getAppid())).get("data");
                GameModel updatedData = objectMapper.readValue(dataNode.toString(), GameModel.class);
                // Update gameModel with the updatedData or apply your logic here
                updatedData.setAppid(gameModel.getAppid());
                {
                    if (updatedData.getScreenshots() != null) {
                        updatedData.getScreenshots().forEach(screenshot -> {
                            screenshot.setGameModel(updatedData);
                        });
                    }
                    if (updatedData.getMovies() != null) {
                        updatedData.getMovies().forEach(movie -> {
                            movie.setGameModel(updatedData);
                        });
                    }

                    gameModelRepository.saveAndFlush(updatedData);

                    log.info("Updated gameModel with appid: {}", gameModel.getAppid());
                }
                ;
            } catch (NullPointerException e) {
                continue;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void getGameDetailAndUpdateIt() {
        List<GameModel> gameModels = gameModelRepository.findAll();
        int batchSize = 100;
        int totalModels = gameModels.size();

        ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the thread pool size as needed

        for (int i = 0; i < totalModels; i += batchSize) {
            List<GameModel> batch = gameModels.subList(i, Math.min(i + batchSize, totalModels));
            CompletableFuture<Void> future = updateGameModels(batch);
            // Wait for the batch update to complete
            future.join();
        }

        executorService.shutdown();
    }

    @Transactional
    public List<GameModel> findAll() {
        return gameModelRepository.findAll();
    }

    @Transactional
    public GameModel findGameByAppid(Long appid) throws NoSuchElementException {
        return gameModelRepository.findById(appid).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + appid));
    }

    @Transactional
    public GameModel findByAppid(Long appid){
        return gameModelRepository.findById(appid).orElseThrow(()->new NoSuchElementException("Not found product with id: "+ appid));
    }

    @Transactional
    public void deleteGameByAppid(Long appid){
        GameModel gameModel = gameModelRepository.findById(appid).orElseThrow(()->new NoSuchElementException("Not found product with id: "+ appid));
        gameModelRepository.deleteById(appid);

    }
    @Transactional
    public void saveProduct(GameModel game){
        gameModelRepository.save(game);
    }
//    @Transactional
//    public void deleteGameByAppid(Long appid){
//        try{
//            GameModel game = gameModelRepository.findById(appid).orElseThrow(()->new NoSuchElementException("Game not found with id: "+appid));
//            gameModelRepository.deleteById(appid);
//            filesService.delete(product.getImage());
//            product.getImages().stream().forEach(image-> {
//                try {
//                    filesService.delete(image);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
}

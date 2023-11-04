package com.greamz.backend.controller;

import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.GameModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameRestController {
    private final GameModelService service;
    @GetMapping("/findAllPagination")
    public ResponseEntity<?> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "7") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }
    @GetMapping("/search")
    public ResponseEntity<Iterable<GameModel>> searchGame(@RequestParam(defaultValue = "") String term,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "7") int size) {
        Iterable<GameModel> gameModels = service.searchGame(term, page, size);
        return ResponseEntity.ok(gameModels);
    }

    @PostMapping("/create")
    public GameModel create(@RequestBody GameModel game){
        service.saveGameModel(game);
        return game;
    }
    @GetMapping("{appid}")
    public ResponseEntity<GameModel> getOne(@PathVariable("appid") Long appid) {
        try {
            GameModel gameModel = service.findGameByAppid(appid);
            return ResponseEntity.ok(gameModel);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{appid}")
    public void delete(@PathVariable("appid") Long appid){
        service.deleteGameByAppid(appid);
    }
//    @GetMapping("/findByCategory/{categoryId}")
//    public ResponseEntity<List<GameModel>> findByCategory(@PathVariable("categoryId") Long categoryId){
//        List<GameModel> gameModels = service.findGameByCategory(categoryId);
//        return ResponseEntity.ok(gameModels);
//    }

}

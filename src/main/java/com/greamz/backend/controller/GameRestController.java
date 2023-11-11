package com.greamz.backend.controller;

import com.greamz.backend.dto.GameFilter;
import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.GameModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameRestController {
    private final GameModelService service;
    @GetMapping("/findAllPagination")
    public ResponseEntity<Page<GameModel>> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "7") int size) {

        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<GameModel>> searchGame(@RequestParam(defaultValue = "") String term,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GameModel> gameModels = service.searchGame(term, pageable);
        return ResponseEntity.ok(gameModels);
    }
    @GetMapping("/filter")
    public ResponseEntity<Page<GameModel>> filter(
            @RequestParam(defaultValue = "") String q,
            @RequestParam (defaultValue = "")String categoriesId,
            @RequestParam(defaultValue = "-1") Long platformId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "") String devices,
            @RequestParam(defaultValue = "-1") Double minPrice,
            @RequestParam (defaultValue = "-1")Double maxPrice,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam (defaultValue = "ASC")Sort.Direction direction){

        Page<GameModel> gameModels = service.filterGamesByCategoriesAndPlatform(q,categoriesId,platformId,page,size,devices,minPrice,maxPrice,sort,direction);
        return ResponseEntity.ok(gameModels);
    }
    @PostMapping("/create")
    public GameModel create(@RequestBody GameModel game){
        service.saveGameModel(game);
        return game;
    }
    @GetMapping("/{appid}")
    public ResponseEntity<GameModel> getOne(@PathVariable("appid") Long appid) {
        try {
            GameModel gameModel = service.findGameByAppid(appid);
            return ResponseEntity.ok(gameModel);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/ids/{appids}")
    public ResponseEntity<List<GameModel>> findGamesByIds(@PathVariable("appids") String appid) {
        System.out.println(appid);
        try {
            List<GameModel> gameModels = service.findGameByGameIds(appid);
            return ResponseEntity.ok(gameModels);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete/{appid}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','EMPLOYEE')")
    public void delete(@PathVariable("appid") Long appid){
        service.deleteGameByAppid(appid);
    }
//    @GetMapping("/findByCategory/{categoryId}")
//    public ResponseEntity<List<GameModel>> findByCategory(@PathVariable("categoryId") Long categoryId){
//        List<GameModel> gameModels = service.findGameByCategory(categoryId);
//        return ResponseEntity.ok(gameModels);
//    }

}

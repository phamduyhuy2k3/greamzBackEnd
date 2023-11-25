package com.greamz.backend.controller;

import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.GameModelService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameRestControllerTest {
    @Mock
    private GameModelService gameModelService;

    @InjectMocks
    private GameRestController gameRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllPagination() {
        Page<GameModel> page = new PageImpl<>(Collections.emptyList());
        when(gameModelService.findAll(any())).thenReturn(page);

        ResponseEntity<Page<GameModel>> response = gameRestController.findAllPagination(0, 7);

        assertEquals(page, response.getBody());
    }

    @Test
    public void testSearchGame() {
        Page<GameModel> page = new PageImpl<>(Collections.emptyList());
        when(gameModelService.searchGame(anyString(), any())).thenReturn(page);

        ResponseEntity<Page<GameModel>> response = gameRestController.searchGame("", 0, 7);

        assertEquals(page, response.getBody());
    }

    @Test
    public void testFilter() {
        Page<GameDetailClientDTO> page = new PageImpl<>(Collections.emptyList());
        when(gameModelService.filterGamesByCategoriesAndPlatform(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString(), any())).thenReturn(page);

        ResponseEntity<Page<GameDetailClientDTO>> response = gameRestController.filter("", "", 0, 7, -1.0, -1.0, "", null);

        assertEquals(page, response.getBody());
    }

    @Test
    public void testCreate() {
        GameModel game = new GameModel(); // Tạo đối tượng GameModel
        when(gameModelService.saveGameModel(any())).thenReturn(game);

        GameModel createdGame = gameRestController.create(game);

        assertEquals(game, createdGame);
    }

    @Test
    public void testGetOne() {
        Long appid = 1L;
        GameModel gameModel = new GameModel(); // Tạo đối tượng GameModel
        when(gameModelService.findGameByAppid(appid)).thenReturn(gameModel);

        ResponseEntity<GameModel> response = gameRestController.getOne(appid);

        assertEquals(gameModel, response.getBody());
    }

    @Test
    public void testFindGamesByIds() {
        String appid = "1,2,3"; // Chuỗi các appid
        List<GameModel> gameModels = Collections.emptyList(); // Danh sách GameModel rỗng
        when(gameModelService.findGameByGameIds(appid)).thenReturn(gameModels);

        ResponseEntity<List<GameModel>> response = gameRestController.findGamesByIds(appid);

        assertEquals(gameModels, response.getBody());
    }

    @Test
    public void testDelete() {
        Long appid = 1L;
        // Không cần trả về giá trị khi gọi phương thức deleteGameByAppid, chỉ cần kiểm tra xem nó có ném ngoại lệ hay không
        gameRestController.delete(appid);
    }
}

package com.greamz.backend.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.service.GameModelService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.FileReader;
import java.util.List;

@Component
public class DataImporter {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private GameModelService gameModelService;

    public void importData(String jsonFilePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:/"+jsonFilePath);
        FileReader fileReader = new FileReader(resource.getFile());
        List<GameModel> gameModels = objectMapper.readValue(fileReader, new TypeReference<List<GameModel>>(){});

        gameModelService.saveGameModelsInBatch(gameModels);
    }
}

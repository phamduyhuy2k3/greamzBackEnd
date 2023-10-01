package com.greamz.backend;

import com.greamz.backend.component.DataImporter;
import com.greamz.backend.service.GameModelService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BackEndApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackEndApplication.class, args);

    }
    @Autowired
    private DataImporter dataImporter;
    @Autowired
    private GameModelService gameModelService;
    @PostConstruct
    public void startImportData() throws Exception {
        dataImporter.importData("response.json");
    }
}

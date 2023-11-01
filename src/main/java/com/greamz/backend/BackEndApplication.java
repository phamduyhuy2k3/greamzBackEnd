package com.greamz.backend;

import com.greamz.backend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableConfigurationProperties(AppProperties.class)
public class BackEndApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackEndApplication.class, args);

    }

}

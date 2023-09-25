package com.greamz.backend.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3ClientConfig {
    @Bean
    public S3Client clientS3(){

        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }
}

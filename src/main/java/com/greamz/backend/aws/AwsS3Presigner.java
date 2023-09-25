package com.greamz.backend.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Presigner {
    @Bean
    public S3Presigner s3Presigner(){

        return S3Presigner.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }
}

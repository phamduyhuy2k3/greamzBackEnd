package com.greamz.backend.aws;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;


@Service
@AllArgsConstructor
public class AwsS3Service {
    private final S3Client s3Client;
    private final S3Presigner presigner;
    public void putObject(String bucketName, String key, MultipartFile file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            s3Client.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getObject(String bucketName, String key) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
        GetObjectPresignRequest getObjectPresignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedGetObjectRequest =
                presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();

    }
    void listObjectsInBucket(String bucketName) {


        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

        List<S3Object> contents = listObjectsV2Response.contents();

        System.out.println("Number of objects in the bucket: " + contents.stream().count());
        contents.stream().forEach(System.out::println);

        s3Client.close();
    }
}

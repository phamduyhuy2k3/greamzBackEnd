package com.greamz.backend.controller;


import com.greamz.backend.aws.AwsS3Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@AllArgsConstructor
public class AwsS3Controller {
    private final AwsS3Service s3Service;
    @PostMapping("/aws/s3")
    public void uploadFile(@RequestParam("file") MultipartFile file){

        s3Service.putObject("pdhawsbucket","test/%s_%s".formatted(new Date(),file.getOriginalFilename()),file);
    }
    @GetMapping("/aws/s3/{key}")
    public String getFile(@PathVariable String key){
        return s3Service.getObject("pdhawsbucket","test/"+key);
    }
}

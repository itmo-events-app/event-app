package org.itmo.eventapp.main.controller;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.minio.MinioService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class TestController {
    private final MinioService minioService;

    @GetMapping("hello")
    ResponseEntity<String> sayHello(@RequestParam String s) {
        return ResponseEntity.ok("Hello, " + s + "!");
    }

    @PutMapping(value = "/upload/{bucket}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> upload(
            @RequestPart
            MultipartFile multipartFile,
            @PathVariable
            String bucket
    ) {
        minioService.upload(multipartFile, bucket);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{bucket}/{object}")
    ResponseEntity<Void> delete(
            @PathVariable
            String bucket,
            @PathVariable
            String object
    ) {
        minioService.delete(bucket, object);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }
}

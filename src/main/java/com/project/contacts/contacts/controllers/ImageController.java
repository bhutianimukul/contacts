package com.project.contacts.contacts.controllers;

import java.util.HashMap;
import java.util.Map;

import com.project.contacts.contacts.Services.S3ImageService;
import com.project.contacts.contacts.Utilities.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageController {
    @Autowired
    S3ImageService imageService;
    @Autowired
    Utils util;

    @PostMapping("/upload")

    public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile file)
            throws Exception {
        String id = util.generateUserId(15);
        Map<String, String> map = new HashMap<>();
        try {

            map.put("url", imageService.upload(file, id));
            map.put("message", "Image uploaded successfully");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

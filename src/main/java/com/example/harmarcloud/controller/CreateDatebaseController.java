package com.example.harmarcloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDatebaseController {
    @PostMapping("/datebase")
    public ResponseEntity<String> createDatebase() {

    }
}

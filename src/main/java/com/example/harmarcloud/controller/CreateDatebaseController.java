package com.example.harmarcloud.controller;

import com.example.harmarcloud.controller.dto.CreateDatabaseRequest;
import com.example.harmarcloud.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateDatebaseController {
    
    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/datebase")
    public ResponseEntity<String> HandleCreateDatebase(
            @RequestBody CreateDatabaseRequest request
    ) {
        try {
            String databaseId = databaseService.createDatabase(
                request.getDatabaseName(),
                request.getSecurityKey(),
                request.getDescription()
            );
            
            return ResponseEntity.ok()
                .body(String.format("Database created successfully with ID: %s", databaseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Failed to create database: " + e.getMessage());
        }
    }
}

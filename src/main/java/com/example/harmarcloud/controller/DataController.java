package com.example.harmarcloud.controller;

import com.example.harmarcloud.model.DataEntry;
import com.example.harmarcloud.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database/{databaseId}/data")
public class DataController {

    @Autowired
    private DataStorageService dataStorageService;

    @PostMapping
    public ResponseEntity<?> createData(
            @PathVariable String databaseId,
            @RequestHeader("Security-Key") String securityKey,
            @RequestBody Map<String, Object> data) {
        try {
            String dataId = dataStorageService.saveData(databaseId, securityKey, data);
            return ResponseEntity.ok().body(Map.of("id", dataId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{dataId}")
    public ResponseEntity<?> getData(
            @PathVariable String databaseId,
            @PathVariable String dataId,
            @RequestHeader("Security-Key") String securityKey) {
        try {
            DataEntry entry = dataStorageService.getData(databaseId, securityKey, dataId);
            return ResponseEntity.ok(entry);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listData(
            @PathVariable String databaseId,
            @RequestHeader("Security-Key") String securityKey) {
        try {
            List<DataEntry> entries = dataStorageService.listData(databaseId, securityKey);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{dataId}")
    public ResponseEntity<?> updateData(
            @PathVariable String databaseId,
            @PathVariable String dataId,
            @RequestHeader("Security-Key") String securityKey,
            @RequestBody Map<String, Object> newData) {
        try {
            dataStorageService.updateData(databaseId, securityKey, dataId, newData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{dataId}")
    public ResponseEntity<?> deleteData(
            @PathVariable String databaseId,
            @PathVariable String dataId) {
        try {
            dataStorageService.deleteData(databaseId, dataId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 
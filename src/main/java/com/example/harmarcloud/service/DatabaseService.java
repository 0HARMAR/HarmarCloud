package com.example.harmarcloud.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class DatabaseService {
    private static final String DATABASE_ROOT = "secure_storage";

    public String createDatabase(String databaseName, String securityKey, String description) throws IOException {
        // 创建一个唯一的数据库标识符
        String databaseId = generateDatabaseId(databaseName, securityKey);
        
        // 创建数据库目录
        Path databasePath = Paths.get(DATABASE_ROOT, databaseId);
        Files.createDirectories(databasePath);

        // 创建元数据文件
        Path metadataPath = databasePath.resolve("metadata.json");
        String metadata = String.format("""
                {
                    "name": "%s",
                    "created": "%s",
                    "description": "%s"
                }""", 
                databaseName, 
                java.time.LocalDateTime.now(),
                description);
        
        Files.writeString(metadataPath, metadata);

        return databaseId;
    }

    private String generateDatabaseId(String databaseName, String securityKey) {
        try {
            String combined = databaseName + ":" + securityKey + ":" + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate database ID", e);
        }
    }
} 
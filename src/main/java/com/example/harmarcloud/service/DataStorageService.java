package com.example.harmarcloud.service;

import com.example.harmarcloud.model.DataEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataStorageService {
    private static final String DATA_DIR = "data";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String saveData(String databaseId, String securityKey, Map<String, Object> data) throws Exception {
        DataEntry entry = new DataEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setData(data);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());

        // 获取数据库目录
        Path dbPath = Paths.get(DatabaseService.DATABASE_ROOT, databaseId);
        if (!Files.exists(dbPath)) {
            throw new IllegalArgumentException("Database not found");
        }

        // 创建数据目录
        Path dataPath = dbPath.resolve(DATA_DIR);
        Files.createDirectories(dataPath);

        // 加密并保存数据
        byte[] encryptedData = encryptData(objectMapper.writeValueAsString(entry), securityKey);
        Path filePath = dataPath.resolve(entry.getId() + ".dat");
        Files.write(filePath, encryptedData);

        return entry.getId();
    }

    public DataEntry getData(String databaseId, String securityKey, String dataId) throws Exception {
        Path filePath = Paths.get(DatabaseService.DATABASE_ROOT, databaseId, DATA_DIR, dataId + ".dat");
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Data not found");
        }

        byte[] encryptedData = Files.readAllBytes(filePath);
        String decryptedJson = decryptData(encryptedData, securityKey);
        return objectMapper.readValue(decryptedJson, DataEntry.class);
    }

    public List<DataEntry> listData(String databaseId, String securityKey) throws Exception {
        Path dataPath = Paths.get(DatabaseService.DATABASE_ROOT, databaseId, DATA_DIR);
        if (!Files.exists(dataPath)) {
            return Collections.emptyList();
        }

        List<DataEntry> entries = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataPath, "*.dat")) {
            for (Path file : stream) {
                byte[] encryptedData = Files.readAllBytes(file);
                String decryptedJson = decryptData(encryptedData, securityKey);
                entries.add(objectMapper.readValue(decryptedJson, DataEntry.class));
            }
        }
        return entries;
    }

    public void updateData(String databaseId, String securityKey, String dataId, Map<String, Object> newData) throws Exception {
        DataEntry entry = getData(databaseId, securityKey, dataId);
        entry.setData(newData);
        entry.setUpdatedAt(LocalDateTime.now());

        byte[] encryptedData = encryptData(objectMapper.writeValueAsString(entry), securityKey);
        Path filePath = Paths.get(DatabaseService.DATABASE_ROOT, databaseId, DATA_DIR, dataId + ".dat");
        Files.write(filePath, encryptedData);
    }

    public void deleteData(String databaseId, String dataId) throws IOException {
        Path filePath = Paths.get(DatabaseService.DATABASE_ROOT, databaseId, DATA_DIR, dataId + ".dat");
        Files.deleteIfExists(filePath);
    }

    private byte[] encryptData(String data, String securityKey) throws Exception {
        SecretKey key = generateKey(securityKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data.getBytes());
    }

    private String decryptData(byte[] encryptedData, String securityKey) throws Exception {
        SecretKey key = generateKey(securityKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    private SecretKey generateKey(String securityKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(securityKey.getBytes());
        return new SecretKeySpec(hash, "AES");
    }
} 
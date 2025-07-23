package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.error.StorageException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileService.class);
    private final ObjectMapper mapper;

    @Value("${files.storageFolder}")
    private String storageFolder;

    public FileService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void storeFile(MultipartFile file) {
        logger.info("Starting upload file");

        if (file.isEmpty()) {
            throw new StorageException("file is empty");
        }

        try {
            Path path = Paths.get(storageFolder + "/" + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            logger.error("Failed to store file");
            throw new StorageException(e.getMessage());
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Country>> parseFileContent(MultipartFile file) {
        try {
            logger.info("Starting parser process with thread: " + Thread.currentThread().getName());
            final List<Country> countries = mapper.readValue( file.getInputStream(), new TypeReference<List<Country>>() {});
            return CompletableFuture.completedFuture(countries);
        } catch (IOException e) {
            logger.error("Failed to parse file content from file " + file.getOriginalFilename());
            throw new StorageException(e.getMessage());
        }
    }
}

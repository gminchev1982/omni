package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.error.FileException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final ObjectMapper mapper;

    @Value("${files.storageFolder}")
    private String storageFolder;

    public FileServiceImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Save file
     * @param file
     */
    public void storeFile(MultipartFile file) {
        logger.info("Starting upload file");

        if (file.isEmpty()) {
            throw new FileException("file is empty");
        }

        try {
            var dir = new File(getStorageDir());
            if (!dir.exists() && !dir.mkdirs()) {
                throw new FileException("Failed to store file: Cannot create directory: " + dir);
            }

            var storageFileName = dir + "/" + file.getOriginalFilename();

            try (FileOutputStream storageOutputStream = new FileOutputStream(storageFileName)) {
                IOUtils.copy(file.getInputStream(), storageOutputStream);
                storageOutputStream.flush();
            }
        } catch (IOException e) {
            logger.error("Failed to store file");
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Async parse file content
     * @param file
     * @return CompletableFuture object
     */
    @Async
    public CompletableFuture<List<CountryDto>> parseFileContent(MultipartFile file) {
        try {
            logger.info("Starting parser process.");
            final var countries =
                    mapper.readValue(file.getInputStream(), new TypeReference<List<CountryDto>>() {});

            return CompletableFuture.completedFuture(countries);
        } catch (IOException e) {
            logger.error("Failed to parse file content from file {}.", file.getOriginalFilename());
            throw new FileException(e.getMessage());
        }
    }

    private String getStorageDir() {
        return storageFolder;
    }
}

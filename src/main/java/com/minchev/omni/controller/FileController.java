package com.minchev.omni.controller;

import com.minchev.omni.entity.Country;
import com.minchev.omni.error.StorageException;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;
    private final CountryService countryService;

    public FileController(FileService fileService, CountryService countryService) {
        this.fileService = fileService;
        this.countryService = countryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        LOGGER.info("starting upload file");

        try {
            fileService.storeFile(file);
            CompletableFuture<List<Country>> parsedContentFuture = fileService.parseFileContent(file);
            parsedContentFuture.thenApply(countries-> countryService.saveCountriesConcurrently(countries));

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}

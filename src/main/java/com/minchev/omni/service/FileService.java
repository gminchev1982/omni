package com.minchev.omni.service;

import com.minchev.omni.dto.CountryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    void storeFile(MultipartFile file);

    CompletableFuture<List<CountryDto>> parseFileContent(MultipartFile file);
}

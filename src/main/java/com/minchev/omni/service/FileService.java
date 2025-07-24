package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    void storeFile(MultipartFile file);

    CompletableFuture<List<Country>> parseFileContent(MultipartFile file);
}

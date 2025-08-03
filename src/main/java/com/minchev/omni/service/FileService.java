package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.entity.FileHistory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    FileHistory storeFile(MultipartFile file);

    CompletableFuture<List<Country>> parseFileContent(MultipartFile file, FileHistory storage);
}

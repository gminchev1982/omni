package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.entity.FileHistory;
import com.minchev.omni.error.FileException;
import com.minchev.omni.repository.FileHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final ObjectMapper mapper;
    private final FileHistoryRepository fileHistoryRepository;

    @Value("${files.directory}")
    private String directory;

    public FileServiceImpl(ObjectMapper mapper, FileHistoryRepository fileHistoryRepository) {
        this.mapper = mapper;
        this.fileHistoryRepository = fileHistoryRepository;
    }

    /**
     * Save file
     *
     * @param file
     */
    public FileHistory storeFile(MultipartFile file) {
        logger.info("Starting upload file");

        if (file.isEmpty()) {
            throw new FileException("file is empty");
        }

        try {
            var dir = new File(getDirectory());
            if (!dir.exists() && !dir.mkdirs()) {
                throw new FileException("Failed to store file: Cannot create directory: " + dir);
            }

            var fileName = generateFileName(file.getOriginalFilename());

            if (fileName == null) {
                throw new FileException("Fina to store file: Cannot create file name");
            }

            Path path = Path.of(dir + "/" + fileName);
            file.transferTo(path);

            return saveFileHistory(file.getOriginalFilename(), fileName);

        } catch (IOException e) {
            logger.error("Failed to store file");
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Async parse file content
     *
     * @param file
     * @return CompletableFuture object
     */
    @Async
    public CompletableFuture<List<Country>> parseFileContent(MultipartFile file, FileHistory fileHistory) {
        try {
            logger.info("Starting parser process.");
            var countries =
                    mapper.readValue(file.getInputStream(), new TypeReference<List<Country>>() {});

            countries.forEach(x-> x.setFileHistory(fileHistory));

            return CompletableFuture.completedFuture(countries);
        } catch (IOException e) {
            logger.error("Failed to parse file content from file {}.", file.getOriginalFilename());
            throw new FileException(e.getMessage());
        }
    }

    private String getDirectory() {
        return directory;
    }

    @Transactional
    private FileHistory saveFileHistory(String originalFilename, String fileName) {
        FileHistory fileHistory = new FileHistory();
        fileHistory.setNameOriginal(originalFilename);
        fileHistory.setName(fileName);

        return fileHistoryRepository.save(fileHistory);
    }

    private String generateFileName(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            return UUID.randomUUID().toString().replace("-", "")
                    + "." + originalFilename.substring(dotIndex + 1);
        }

        return null;
    }

}

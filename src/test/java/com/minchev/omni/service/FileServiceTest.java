package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.error.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    ObjectMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void storeFile_ExceptionEmptyFile() throws IOException {

        String jsonString = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";
        MultipartFile multipartFile = new MockMultipartFile("sos.json", "".getBytes());

        StorageException exception = assertThrows(StorageException.class
                , () -> {
                    fileService.storeFile(multipartFile);
        });

        assertEquals(exception.getMessage(), "file is empty");
    }

    @Test
    public void parseFileContent() throws ExecutionException, InterruptedException, IOException {
        Country country = new Country();
        country.setCode("YE");
        country.setName("Yemen");

        String jsonString = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";
        MultipartFile multipartFile = new MockMultipartFile("sos.json", jsonString.getBytes());

        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(List.of(country));

        var result = fileService.parseFileContent(multipartFile);

        assertEquals(1, result.get().size());
        assertEquals(country.getName(), result.get().get(0).getName());
    }

    @Test
    public void parseFileContent_Exception() throws IOException, ExecutionException, InterruptedException {
        String jsonString = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";
        MultipartFile multipartFile = new MockMultipartFile("sos.json", jsonString.getBytes());

        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(new IOException());

        var result = fileService.parseFileContent(multipartFile);

        CompletionException exception = assertThrows(CompletionException.class, result::join);
        assertTrue(exception.getCause() instanceof IOException);
    }
}

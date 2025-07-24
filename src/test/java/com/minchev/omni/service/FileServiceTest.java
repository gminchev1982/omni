package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.error.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @TempDir
    Path tempDir;

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

        StorageException exception = assertThrows(StorageException.class, () -> {
                    fileService.storeFile(multipartFile);
        });

        assertEquals(exception.getMessage(), "file is empty");
    }

    @Test
    public void storeFile_saved() throws IOException, NoSuchFieldException, IllegalAccessException {
        String jsonString = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";
        String nameFile = "sos.json";

        MultipartFile multipartFile =
                new MockMultipartFile(nameFile, nameFile, "text/plain", jsonString.getBytes());

        Field storageFolderField = FileServiceImpl.class.getDeclaredField("storageFolder");
        storageFolderField.setAccessible(true);
        storageFolderField.set(fileService, tempDir.toString());

        fileService.storeFile(multipartFile);

        File file = new File(tempDir.toString() + "/" + "sos.json");

        assertTrue(file.isFile());
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

        StorageException storageException = assertThrows(StorageException.class, () -> {
                fileService.parseFileContent(multipartFile);
        });

        assertTrue(storageException != null);
    }
}

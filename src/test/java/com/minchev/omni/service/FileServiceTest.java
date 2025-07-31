package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.error.FileException;
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
        var file = new MockMultipartFile("sos.json", "".getBytes());

        FileException exception = assertThrows(FileException.class, () -> {
            fileService.storeFile(file);
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
    public void parseFileContent_done() throws ExecutionException, InterruptedException, IOException {
        Country country = new Country();
        country.setCode("YE");
        country.setName("Yemen");

        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(List.of(country));

        var result = fileService.parseFileContent(mockMultipartFile());

        assertEquals(1, result.get().size());
        assertEquals(country.getName(), result.get().get(0).getName());
    }

    @Test
    public void parseFileContent_wrongData_Exception() throws IOException, ExecutionException, InterruptedException {
        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(new IOException());

        FileException storageException = assertThrows(FileException.class, () -> {
                fileService.parseFileContent(mockMultipartFile());
        });

        assertTrue(storageException != null);
    }

    private MultipartFile mockMultipartFile() {
        String jsonString = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";
        return new MockMultipartFile("sos.json", jsonString.getBytes());
    }
}

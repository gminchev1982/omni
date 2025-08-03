package com.minchev.omni.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.entity.FileHistory;
import com.minchev.omni.error.FileException;
import com.minchev.omni.repository.FileHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class FileServiceTest {

    private static final String FILE_NAME = "sos.json";
    private static final String FILE_CONTENT = "[{\"name\": \"Yemen\", \"code\": \"YE\"}]";

    @InjectMocks
    private FileServiceImpl fileService;

    @TempDir
    Path tempDir;

    @Mock
    ObjectMapper mapper;

    @Mock
    private FileHistoryRepository storageRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void storeFile_ExceptionEmptyFile() throws IOException {
        var file = new MockMultipartFile(FILE_NAME, "".getBytes());

        var exception = assertThrows(FileException.class, () -> {
            fileService.storeFile(file);
        });

        assertEquals(exception.getMessage(), "file is empty");
    }

    @Test
    public void storeFile_saved() throws IOException, NoSuchFieldException, IllegalAccessException {

        var fileHistory  = mockFileHistory();
        var multipartFile =
                new MockMultipartFile(FILE_NAME, FILE_NAME, "text/plain", FILE_CONTENT.getBytes());

        var storageFolderField = FileServiceImpl.class.getDeclaredField("directory");
        storageFolderField.setAccessible(true);
        storageFolderField.set(fileService, tempDir.toString());

        when(storageRepository.save(any(FileHistory.class))).thenReturn(fileHistory);

        var result = fileService.storeFile(multipartFile);

        assertEquals(fileHistory.getNameOriginal(), result.getNameOriginal());
    }

    @Test
    public void parseFileContent_done() throws ExecutionException, InterruptedException, IOException {
        var country = new Country();
        country.setCode("YE");
        country.setName("Yemen");

        var fileHistory = mockFileHistory();

        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(List.of(country));
        when(storageRepository.save(any(FileHistory.class))).thenReturn(fileHistory);

        var result = fileService.parseFileContent(mockMultipartFile(), fileHistory);

        assertEquals(1, result.get().size());
        assertEquals(country.getName(), result.get().get(0).getName());
        assertEquals(country.getFileHistory(), result.get().get(0).getFileHistory());
    }

    @Test
    public void parseFileContent_wrongData_Exception() throws IOException, ExecutionException, InterruptedException {
        when(mapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(new IOException());

        var storageException = assertThrows(FileException.class, () -> {
            fileService.parseFileContent(mockMultipartFile(), mockFileHistory());
        });

        assertTrue(storageException != null);
    }

    private MultipartFile mockMultipartFile() {
        return new MockMultipartFile(FILE_NAME, FILE_CONTENT.getBytes());
    }

    private FileHistory mockFileHistory() {
        var fileHistory = new FileHistory();
        fileHistory.setId(1L);
        fileHistory.setNameOriginal(FILE_NAME);

        return fileHistory;
    }

}

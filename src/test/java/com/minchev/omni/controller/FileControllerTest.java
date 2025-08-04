package com.minchev.omni.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.entity.FileHistory;
import com.minchev.omni.service.CountryServiceImpl;
import com.minchev.omni.service.FileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private CountryServiceImpl countryService;

    @Mock
    private FileServiceImpl fileService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    public void handleFileUpload_Exception () throws Exception {

        var file = new MockMultipartFile(
                "file",
                "fail.txt",
                "text/plain",
                "".getBytes()
        );

        mockMvc.perform(multipart("/upload")
                .file(file)).andExpect(status().is5xxServerError());
    }

    @Test
    public void handleFileUpload_ok() throws Exception {

        var file = new MockMultipartFile(
                "file",
                "fail.txt",
                "text/plain",
                "".getBytes()
        );

        when(fileService.storeFile(any(MultipartFile.class))).thenReturn(FileHistory.builder().build());
        when(fileService.parseFileContent(any(MultipartFile.class), any(FileHistory.class)))
                .thenReturn(CompletableFuture.completedFuture(List.of(Country.builder().build())));

        mockMvc.perform(multipart("/upload")
                .file(file)).andExpect(status().isOk());
    }

}
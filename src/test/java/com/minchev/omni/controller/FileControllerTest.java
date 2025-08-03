package com.minchev.omni.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minchev.omni.entity.Country;
import com.minchev.omni.entity.FileHistory;
import com.minchev.omni.service.CountryServiceImpl;
import com.minchev.omni.service.FileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private CountryServiceImpl countryService;

    @Mock
    private FileServiceImpl fileService;

    @Mock
    private ObjectMapper mapper;

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

}
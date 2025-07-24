package com.minchev.omni.controller;

import com.minchev.omni.error.StorageException;
import com.minchev.omni.service.CountryService;
import com.minchev.omni.service.FileService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
public class FileControllerTest {

    @MockitoBean
    private FileController fileController;

    @MockitoBean
    private CountryService countryService;

    @MockitoBean
    private FileService fileService;

    @Autowired
    private MockMvc mockMvc;

    @TempDir
    Path tempDir;

    @Test
    public void handleFileUpload_Exception () throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "fail.txt",
                "text/plain",
                "".getBytes()
        );

        mockMvc.perform(multipart("/upload").file(file)).andExpect(status().isOk());
    }
}

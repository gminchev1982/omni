package com.minchev.omni.service;

import com.minchev.omni.config.AsyncConfig;
import com.minchev.omni.entity.Country;
import com.minchev.omni.error.StorageException;
import com.minchev.omni.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AsyncConfig.class})
public class CountryServiceTest {

    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveCountriesAsync(){
        Country country = new Country();
        country.setCode("aa");
        country.setName("bbb");

        when(countryRepository.saveAll(anyList())).thenReturn(List.of(country));

        countryService.saveCountriesAsync(List.of(country));

        verify(countryRepository, times(1)).saveAll(List.of(country));
    }

    @Test
    public void saveCountriesAsync_retry(){

        Country country = new Country();
        country.setCode("aa");
        country.setName("bbb");

        when(countryRepository.saveAll(anyList()))
                .thenThrow(new DataAccessException("Save failed"){})
                .thenThrow(new DataAccessException("Save failed"){})
                .thenThrow(new DataAccessException("Save failed"){});

        countryService.saveCountriesAsync(List.of(country));

        verify(countryRepository, times(1)).saveAll(anyList());
    }




}

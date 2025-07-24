package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class CountryServiceTest {

    @InjectMocks
    private CountryServiceImpl countryService;

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
}

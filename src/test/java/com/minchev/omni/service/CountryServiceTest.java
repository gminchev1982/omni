package com.minchev.omni.service;

import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.entity.Country;
import com.minchev.omni.mapper.CountryMapper;
import com.minchev.omni.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class CountryServiceTest {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveCountriesAsync_withoutError_saveData() {
        CountryDto countryDto = new CountryDto();
        countryDto.setCode("aa");
        countryDto.setName("bbb");

        when(countryRepository.saveAll(anyList())).thenReturn(List.of(mockCountry()));

        countryService.saveCountriesAsync(List.of(countryDto));

        verify(countryRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void getCountries() {
        var listCountries = List.of(mockCountry());
        when(countryRepository.findAll()).thenReturn(listCountries);

        var result = countryService.getCountries();

        assertEquals(result.size(), listCountries.size());
        assertEquals(result.get(0).getName(), listCountries.get(0).getName());
    }


    private Country mockCountry() {
        Country country = new Country();
        country.setId(1L);
        country.setCode("aa");
        country.setName("bbb");

        return country;
    }
}

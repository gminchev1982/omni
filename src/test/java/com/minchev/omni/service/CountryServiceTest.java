package com.minchev.omni.service;

import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import com.minchev.omni.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void saveCountriesAsync_withoutError_saveData() {
        when(countryRepository.saveAll(anyList())).thenReturn(List.of(mockCountry()));

        var result =  countryService.saveCountriesAsync(List.of(mockCountry()));
        result.join();

        verify(countryRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void saveCountriesAsync_withoutError_saveData22() {
        when(countryRepository.saveAll(anyList())).thenThrow(new DataAccessResourceFailureException("sa"));

        var exception = assertThrows(DataAccessResourceFailureException.class, () -> {
            var exp =  countryService.saveCountriesAsync(List.of(mockCountry()));
            exp.join();
        });

        assertNotNull(exception.getMessage());
    }

    @Test
    public void getCountryShareList_foundCountry_list() {
        var countryShare = new CountryShareDto(mockCountry().getName());
        var listCountries = List.of(countryShare);
        var pageable = PageRequest.of(1, 10);

        when(countryRepository.getCountryShareList(any(Pageable.class))).thenReturn(listCountries);


        var result = countryService.getCountryShareList(pageable);

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

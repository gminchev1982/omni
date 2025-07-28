package com.minchev.omni.service;

import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.entity.Country;

import java.util.List;

public interface CountryService {

    void saveCountriesAsync(List<CountryDto> countries);

    List<Country> getCountries();
}

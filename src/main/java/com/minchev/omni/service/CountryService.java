package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {

    void saveCountriesAsync(List<Country> countries);

    List<Country> getCountyByPage();
}

package com.minchev.omni.service;

import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CountryService {

    CompletableFuture<List<Country>> saveCountriesAsync(List<Country> countries);

    List<CountryShareDto> getCountryShareList(Pageable pageable);
}

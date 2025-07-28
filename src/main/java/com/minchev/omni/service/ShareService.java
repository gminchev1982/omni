package com.minchev.omni.service;

import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShareService {

    ResponseEntity<String> shareData(List<CountryShareDto> countryList) throws Exception;
}

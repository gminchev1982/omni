package com.minchev.omni.service;

import com.minchev.omni.dto.CountryShareDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HttpService {

    ResponseEntity<String> callShareData(String url,
                                         String headerAPiKey,
                                         HttpMethod httpMethod,
                                         List<CountryShareDto> countryShareDtos);
}

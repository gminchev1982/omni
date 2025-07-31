package com.minchev.omni.service;

import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HttpServiceImpl implements HttpService {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);
    public static final String HEADER_X_API_KEY = "x-api-key";

    private final RestTemplate restTemplate;

    public HttpServiceImpl(RestTemplate restTemplate) {
        this.restTemplate  = restTemplate;
    }

    @Override
    public ResponseEntity<String> callShareData(String url, String headerAPiKey, HttpMethod httpMethod, List<CountryShareDto> countryShareDtos) {
        try {
            var headers = new HttpHeaders();
            headers.set(HEADER_X_API_KEY, headerAPiKey);

            var httpEntity = new HttpEntity<>(countryShareDtos.toString(), headers);

            return restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        } catch (HttpServerErrorException e) {
            logger.error("Server not responding:" + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server not responding.");
        }
    }
}

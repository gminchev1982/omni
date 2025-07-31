package com.minchev.omni.service;

import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {

    private final ShareConfigProperties properties;
    private final HttpService httpService;

    public ShareServiceImpl(ShareConfigProperties properties, HttpService httpService) {
        this.properties = properties;
        this.httpService = httpService;

    }

    /**
     * get share data response
     * @param countryShareDtos - list of share data
     * @return responseEntity
     */
    @Override
    public ResponseEntity<String> shareData(List<CountryShareDto> countryShareDtos) {
        return httpService.callShareData(properties.getUrl(), properties.getKey(), HttpMethod.POST, countryShareDtos);
    }
}

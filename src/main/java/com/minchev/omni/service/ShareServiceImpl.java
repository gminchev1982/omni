package com.minchev.omni.service;

import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {

    private static final Logger logger = LoggerFactory.getLogger(ShareServiceImpl.class);
    private static final String HEADER_X_API_KEY = "x-api-key";

    private final RestTemplate restTemplate;
    private final ShareConfigProperties properties;

    public ShareServiceImpl(RestTemplate restTemplate, ShareConfigProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * send data to an external server
     * @param countryList - list of {@link Country}
     * @return - ResponseEntity<String>
     * @throws - HttpServerErrorException
     */
    @Override
    public ResponseEntity<String> shareData(List<Country> countryList) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HEADER_X_API_KEY, properties.getKey());

            HttpEntity<String> httpEntity = new HttpEntity<>(countryList.toString(), headers);

            return restTemplate.exchange(properties.getUrl(), HttpMethod.POST, httpEntity, String.class);
        } catch (HttpServerErrorException e) {
            logger.error("Server is not responded:");
            throw new RuntimeException("send");
        }
    }
}

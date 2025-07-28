package com.minchev.omni.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.error.FileException;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
public class ShareServiceTest {

    @Autowired
    private ShareServiceImpl shareService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ShareConfigProperties properties;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shareData() {
        mockServer.expect(requestTo("http://localhost/share")).andRespond(withSuccess());

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shareData_withException() {
        mockServer.expect(requestTo("http://localhost/share")).andRespond(withServerError());

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

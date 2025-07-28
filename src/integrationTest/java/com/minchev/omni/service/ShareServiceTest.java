package com.minchev.omni.service;

import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.EnableWireMock;

import java.util.List;

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
    public void shareData_serverRespond_200() {
        mockServer.expect(requestTo("http://localhost/share")).andRespond(withSuccess());

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shareData_serverNotRespond_500() {
        mockServer.expect(requestTo("http://localhost/share")).andRespond(withServerError());

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.minchev.omni.service;

import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HttpServiceTest {

    public static final String CALL_URL = "http://localhost/share";
    public static final String CALL_KEY = "key";

    @Autowired
    private HttpService httpService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    public void call_serverRespond_200() {
        mockServer.expect(requestTo(CALL_URL)).andRespond(withSuccess());

        var result =
                httpService.callShareData(CALL_URL, CALL_KEY, HttpMethod.POST, List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void call_serverNotRespond_500() {
        mockServer.expect(requestTo("http://localhost/share")).andRespond(withServerError());

        var result =
                httpService.callShareData(CALL_URL, CALL_KEY, HttpMethod.POST, List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package com.minchev.omni.service;


import com.minchev.omni.config.ShareConfigProperties;
import com.minchev.omni.dto.CountryShareDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class ShareServiceTest {

    @InjectMocks
    private ShareServiceImpl shareService;

    @Mock
    private HttpService httpService;

    @Mock
    private ShareConfigProperties properties;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shareData_withResponse_sharedData() throws NoSuchFieldException, IllegalAccessException {
        String url = "http://localhost/share";

        when(httpService.callShareData(any(), any(), any(HttpMethod.class), anyList())).thenReturn(ResponseEntity.ok("so"));

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shareData_withErrorResponse_notShareData() throws NoSuchFieldException, IllegalAccessException {
        when(httpService.callShareData(any(), any(), any(HttpMethod.class), anyList()))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("so"));

        var result = shareService.shareData(List.of(new CountryShareDto()));

        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

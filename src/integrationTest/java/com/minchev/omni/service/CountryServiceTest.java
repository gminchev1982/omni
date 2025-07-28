package com.minchev.omni.service;


import com.minchev.omni.entity.Country;
import com.minchev.omni.repository.CountryRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableRetry
@SpringBootTest
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @MockitoBean
    private CountryRepository countryRepository;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void saveCountriesAsync_dataIsNotValidate_startRetryProcess(){

        Country country = new Country();
        country.setCode(null);
        country.setName(null);

        when(countryRepository.saveAll(anyList())).thenThrow(new DataAccessResourceFailureException("DB always fails"));


        countryService.saveCountriesAsync(List.of(country));

        verify(countryRepository, times(3)).saveAll(anyList());
    }

}

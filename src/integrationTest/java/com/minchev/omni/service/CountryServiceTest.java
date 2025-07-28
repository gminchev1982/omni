package com.minchev.omni.service;


import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.repository.CountryRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    public void saveCountriesAsync_dataIsNotValidate_startRetryProcess(){
    //TODO fix
        CountryDto country = new CountryDto();
        country.setCode(null);
        country.setName(null);

        countryService.saveCountriesAsync(List.of(country));

        verify(countryRepository, times(3)).saveAll(anyList());
    }

}

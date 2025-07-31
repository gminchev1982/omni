package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableRetry
@EnableAsync
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @MockitoSpyBean
    private CountryRepository countryRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveCountriesAsync_startRetryProcess() throws ExecutionException, InterruptedException {
        Country country = new Country();
        country.setCode(null);
        country.setName(null);

        var exception = assertThrows(CompletionException.class, () -> {
            var exp =  countryService.saveCountriesAsync(List.of(country));
            exp.join();
        });

       // assertNotNull(exception.getMessage());
        verify(countryRepository, Mockito.times(3)).saveAll(anyList());
    }

    @Test
    public void saveCountriesAsync_withCorrectData_return() throws ExecutionException, InterruptedException {
        Country country = new Country();
        country.setCode("Sps");
        country.setName("sps");

        var exp =  countryService.saveCountriesAsync(List.of(country));
        exp.join();

        assertEquals(exp.get().size(), 1);
    }

}

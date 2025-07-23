package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.error.CountryException;
import com.minchev.omni.repository.CountryRepository;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CountryService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public void saveCountriesAsync(CompletableFuture<List<Country>> countries) {
        try {
            logger.info("Starting save  - " + Thread.currentThread().getName());
            CompletableFuture<List<Country>> c = CompletableFuture.completedFuture(countryRepository.saveAll(countries.get()));
        } catch (ExecutionException | InterruptedException e) {
           logger.error("Save data is failed : {}", e.getMessage());
           throw new CountryException(e.getMessage());
        }
    }
}

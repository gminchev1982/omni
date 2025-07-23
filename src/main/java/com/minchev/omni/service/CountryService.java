package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.error.CountryException;
import com.minchev.omni.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async
    @Transactional
    public void saveCountriesAsync(CompletableFuture<List<Country>> countries) {
        try {
            logger.info("Starting save data");
            CompletableFuture<List<Country>> future = CompletableFuture.completedFuture(countryRepository.saveAll(countries.get()));
            future.thenAccept(value -> {
               logger.info("Data save completed successfully");
            });
        } catch (ExecutionException | InterruptedException e) {
           logger.error("Data save is failed : {}", e.getMessage());
           throw new CountryException(e.getMessage());
        }
    }
}

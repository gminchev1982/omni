package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.error.CountryException;
import com.minchev.omni.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async
    @Transactional
    @Retryable(
            value = {CountryException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void saveCountriesAsync(List<Country> countries) {
        try {
            logger.info("Starting saving process.");

            var future =
                    CompletableFuture.completedFuture(countryRepository.saveAll(countries));

            future.thenAccept(value -> {
                logger.info("Data save completed successfully.");
            });
        } catch (IllegalArgumentException | DataAccessException e) {
            logger.error("Data save is failed : {}.", e.getMessage());
            throw new CountryException(e.getMessage());
        }
    }
}

package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Async save data
     * @param countries - list of {@link Country}
     */
    @Async
    @Transactional
    @Retryable(
            value = {DataAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
     public void saveCountriesAsync(List<Country> countries) {
            logger.info("Starting data save process.");

            var future = CompletableFuture.completedFuture(countryRepository.saveAll(countries));

            future.thenAccept(value -> {
                logger.info("Data save completed successfully.");
            });
    }

    /**
     * Get a message after a retry mechanism
     * @param e - DataAccessException
     */
    @Recover
    public void recover(DataAccessException e) {
        logger.error("Retry failed: {}" , e.getMessage());
    }

}

package com.minchev.omni.service;

import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.entity.Country;
import com.minchev.omni.mapper.CountryMapper;
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
    private final CountryMapper countryMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
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
    public void saveCountriesAsync(List<CountryDto> countries) {
        logger.info("Starting data save process.");

        var countryList = countryMapper.toCountryList(countries);
        var future =
                CompletableFuture.completedFuture(countryRepository.saveAll(countryList));

        future.thenAccept(value -> {
            logger.info("Data save completed successfully.");
        });
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findAll();
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

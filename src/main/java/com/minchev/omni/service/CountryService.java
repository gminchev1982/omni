package com.minchev.omni.service;

import com.minchev.omni.entity.Country;
import com.minchev.omni.error.CountryException;
import com.minchev.omni.repository.CountryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async("taskExecutor")
    @Transactional
    private CompletableFuture<Country> saveCountryAsync(Country country) {
        System.out.println("Execute method with configured executor - "
                + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(countryRepository.save(country));
    }

    public CompletableFuture<List<Country>> saveCountriesConcurrently(List<Country> countries) {
        try {
            List<CompletableFuture<Country>> futures = countries.stream().map(this::saveCountryAsync)
                    .collect(Collectors.toList());

            // Combine all futures into one
            CompletableFuture<Void> allDone = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );

            return allDone.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList())
            );

        } catch (Exception e) {
           throw new CountryException(e.getMessage());
        }
    }
}

package com.minchev.omni.repository;

import com.minchev.omni.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> saveAll(List<Country> countries);
}

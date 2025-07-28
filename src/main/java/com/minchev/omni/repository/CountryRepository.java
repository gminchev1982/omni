package com.minchev.omni.repository;

import com.minchev.omni.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findBy(Pageable pageable);
    Optional <Country> findByNameAndCode(String name, String code);
}

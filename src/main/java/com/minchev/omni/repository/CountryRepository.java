package com.minchev.omni.repository;

import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT new com.minchev.omni.dto.CountryShareDto(c.name) FROM Country c")
    List<CountryShareDto> getCountryShareList(Pageable pageable);




}

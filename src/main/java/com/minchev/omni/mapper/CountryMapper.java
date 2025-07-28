package com.minchev.omni.mapper;

import com.minchev.omni.dto.CountryDto;
import com.minchev.omni.dto.CountryShareDto;
import com.minchev.omni.entity.Country;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CountryMapper {

    public abstract List<Country> toCountryList(List<CountryDto> countryList);

    public abstract List<CountryShareDto> toCountryShareList(List<Country> countryList);
}

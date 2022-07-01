package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.CountryDto;

import java.util.List;
import java.util.Map;

public interface CountryService {

    List<CountryDto> getAll();
    CountryDto getById(Long id);
    void create(CountryDto countryDto);
    void update(CountryDto countryDto);
    void deleteById(Long id);
    List<CountryDto> search(Map<String, String> query);
    List<CountryDto> searchByString(String search);
}

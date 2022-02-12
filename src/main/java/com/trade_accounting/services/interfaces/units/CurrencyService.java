package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.CurrencyDto;

import java.util.List;
import java.util.Map;

public interface CurrencyService {

    List<CurrencyDto> getAll();

    List<CurrencyDto> search(Map<String, String> query);

    CurrencyDto getById(Long id);

    void create(CurrencyDto currencyDto);

    void update(CurrencyDto currencyDto);

    void deleteById(Long id);

    List<CurrencyDto> findBySearch(String search);
}

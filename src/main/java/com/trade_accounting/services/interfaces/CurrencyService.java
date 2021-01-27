package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto> getAll();

    CurrencyDto getById(Long id);

    void create(CurrencyDto currencyDto);

    void update(CurrencyDto currencyDto);

    void deleteById(Long id);
}

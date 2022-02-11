package com.trade_accounting.services.interfaces.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseForecastDto;

import java.util.List;
import java.util.Map;

public interface PurchaseForecastService {
    List<PurchaseForecastDto> getAll();

    PurchaseForecastDto getById(Long id);

    void create(PurchaseForecastDto purchaseCurrentBalanceDto);

    void update(PurchaseForecastDto purchaseCurrentBalanceDto);

    void deleteById(Long id);

    List<PurchaseForecastDto> search(String query);

    List<PurchaseForecastDto> searchByFilter(Map<String, String> query);
}

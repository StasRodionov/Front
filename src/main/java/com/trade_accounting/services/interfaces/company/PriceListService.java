package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.PriceListDto;

import java.util.List;
import java.util.Map;

public interface PriceListService {

    List<PriceListDto> getAll();

    PriceListDto getById(Long id);

    PriceListDto create(PriceListDto priceListDto);

    void update(PriceListDto priceListDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

    List<PriceListDto> quickSearch(String text);

    List<PriceListDto> searchByFilter(Map<String, String> query);

    List<PriceListDto> searchByBetweenDataFilter(Map<String, String> query);
}

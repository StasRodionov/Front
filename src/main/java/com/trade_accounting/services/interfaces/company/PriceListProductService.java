package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.PriceListProductDto;

import java.util.List;
import java.util.Map;

public interface PriceListProductService {
    List<PriceListProductDto> getAll();

    PriceListProductDto getById(Long id);

    void create(PriceListProductDto priceListProductDto);

    void update(PriceListProductDto priceListProductDto);

    void deleteById(Long id);

    List<PriceListProductDto> getByPriceListId(Long id);

    List<PriceListProductDto> search(Map<String, String> query);

    List<PriceListProductDto> getByProductId(Long id);

    List<PriceListProductDto> quickSearch(String text);
}

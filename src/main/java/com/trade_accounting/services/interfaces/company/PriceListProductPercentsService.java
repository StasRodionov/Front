package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;

import java.util.List;
import java.util.Map;

public interface PriceListProductPercentsService {
    List<PriceListProductPercentsDto> getAll();

    PriceListProductPercentsDto getById(Long id);

    PriceListProductPercentsDto create(PriceListProductPercentsDto priceListProductPercentsDto);

    void update(PriceListProductPercentsDto priceListProductPercentsDto);

    void deleteById(Long id);

    List<PriceListProductPercentsDto> getByPriceListId(Long id);

    List<PriceListProductPercentsDto> search(Map<String, String> query);

}

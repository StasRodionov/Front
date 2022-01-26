package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailReturnsDto;

import java.util.List;
import java.util.Map;

public interface RetailReturnsService {

    List<RetailReturnsDto> getAll();

    RetailReturnsDto getById(Long id);

    void create(RetailReturnsDto retailReturnsDto);

    void update(RetailReturnsDto retailReturnsDto);

    void deleteById(Long id);

    List<RetailReturnsDto> search(String query);

    List<RetailReturnsDto> searchRetailReturns(Map<String, String> queryRetailReturns);
}

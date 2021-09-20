package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailReturnsDto;

import java.util.List;

public interface RetailReturnsService {

    List<RetailReturnsDto> getAll();

    RetailReturnsDto getById(Long id);

    void create(RetailReturnsDto retailReturnsDto);

    void update(RetailReturnsDto retailReturnsDto);

    void deleteById(Long id);
}

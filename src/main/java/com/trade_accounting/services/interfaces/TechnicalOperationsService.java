package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TechnicalOperationsDto;

import java.util.List;

public interface TechnicalOperationsService {

    List<TechnicalOperationsDto> getAll();

    TechnicalOperationsDto getById(Long id);

    void create(TechnicalOperationsDto dto);

    void update(TechnicalOperationsDto dto);

    void deleteById(Long id);
}

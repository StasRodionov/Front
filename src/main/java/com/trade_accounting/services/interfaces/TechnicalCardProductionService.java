package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TechnicalCardProductionDto;

import java.util.List;

public interface TechnicalCardProductionService {

    List<TechnicalCardProductionDto> getAll();

    TechnicalCardProductionDto getById(Long id);

    void create(TechnicalCardProductionDto dto);

    void update(TechnicalCardProductionDto dto);

    void deleteById(Long id);
}

package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.InvoiceProductDto;

import java.util.List;

public interface AcceptanceProductionService {

    List<AcceptanceProductionDto> getAll();

    List<AcceptanceProductionDto> getByAcceptanceId(Long id);

    AcceptanceProductionDto getById(Long id);

    void create(AcceptanceProductionDto acceptanceProductionDto);

    void update(AcceptanceProductionDto acceptanceProductionDto);

    void deleteById(Long id);
}

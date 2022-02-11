package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.AttributeOfCalculationObjectDto;

import java.util.List;

public interface AttributeOfCalculationObjectService {

    List<AttributeOfCalculationObjectDto> getAll();

    AttributeOfCalculationObjectDto getById(Long id);

    void create(AttributeOfCalculationObjectDto attribute);

    void update(AttributeOfCalculationObjectDto attribute);

    void deleteById(Long id);
}

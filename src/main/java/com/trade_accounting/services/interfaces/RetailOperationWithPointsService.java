package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailOperationWithPointsDto;

import java.util.List;

public interface RetailOperationWithPointsService {

    List<RetailOperationWithPointsDto> getAll();

    RetailOperationWithPointsDto getById(Long id);

    void create(RetailOperationWithPointsDto retailOperationWithPointsDto);

    void update(RetailOperationWithPointsDto retailOperationWithPointsDto);

    void deleteById(Long id);
}

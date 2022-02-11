package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailPointsDto;

import java.util.List;

public interface RetailPointsService {

    List<RetailPointsDto> getAll();

    RetailPointsDto getById(Long id);

    void create(RetailPointsDto retailPointsDto);

    void update(RetailPointsDto retailPointsDto);

    void deleteById(Long id);
}
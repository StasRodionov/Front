package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.RetailPointsDto;

import java.util.List;

public interface RetailPointsService {

    List<RetailPointsDto> getAll();

    RetailPointsDto getById(Long id);

    void create(RetailPointsDto retailPointsDto);

    void update(RetailPointsDto retailPointsDto);

    void deleteById(Long id);
}
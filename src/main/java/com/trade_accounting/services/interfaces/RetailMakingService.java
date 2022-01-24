package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.RetailMakingDto;

import java.util.List;

public interface RetailMakingService {

    List<RetailMakingDto> getAll();

    RetailMakingDto getById(Long id);

    void create(RetailMakingDto retailMakingDto);

    void update(RetailMakingDto retailMakingDto);

    void deleteById(Long id);
}

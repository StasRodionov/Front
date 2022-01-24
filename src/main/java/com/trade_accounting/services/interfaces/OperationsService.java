package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.OperationsDto;

import java.util.List;

public interface OperationsService {
    List<OperationsDto> getAll();

    OperationsDto getById(Long id);
}

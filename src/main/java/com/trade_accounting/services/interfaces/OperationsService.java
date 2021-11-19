package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.OperationsDto;

import java.security.Principal;
import java.util.List;

public interface OperationsService {
    List<OperationsDto> getAll();

    OperationsDto getById(Long id);
}

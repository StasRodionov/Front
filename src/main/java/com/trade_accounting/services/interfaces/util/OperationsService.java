package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.OperationsDto;

import java.util.List;
import java.util.Map;

public interface OperationsService {

    List<OperationsDto> getAll();

    OperationsDto getById(Long id);

    List<OperationsDto> searchByFilter(Map<String, String> queryOperations);

    List<OperationsDto> quickSearch(String search);

    List<OperationsDto> quickSearchRecycle(String search);
}

package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InternalOrderDto;

import java.util.List;
import java.util.Map;

public interface InternalOrderService {
    List<InternalOrderDto> getAll();

    InternalOrderDto getById(Long id);

    InternalOrderDto create(InternalOrderDto internalOrderDto);

    void update(InternalOrderDto internalOrderDto);

    void deleteById(Long id);

    List<InternalOrderDto> searchByFilter(Map<String, String> query);
}

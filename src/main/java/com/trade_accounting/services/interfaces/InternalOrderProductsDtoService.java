package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InternalOrderProductsDto;

import java.util.List;

public interface InternalOrderProductsDtoService {

    InternalOrderProductsDto create(InternalOrderProductsDto internalOrderProductsDto);

    InternalOrderProductsDto getById(Long id);

    List<InternalOrderProductsDto> getAll();
}

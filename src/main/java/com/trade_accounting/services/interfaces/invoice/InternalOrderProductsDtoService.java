package com.trade_accounting.services.interfaces.invoice;

import com.trade_accounting.models.dto.invoice.InternalOrderProductsDto;

import java.util.List;

public interface InternalOrderProductsDtoService {

    InternalOrderProductsDto create(InternalOrderProductsDto internalOrderProductsDto);

    InternalOrderProductsDto getById(Long id);

    List<InternalOrderProductsDto> getAll();
}

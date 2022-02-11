package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.LossProductDto;

import java.util.List;

public interface LossProductService {
    List<LossProductDto> getAll();

    LossProductDto getById(Long id);

    LossProductDto create(LossProductDto lossProductDto);

    void update(LossProductDto lossProductDto);

    void deleteById(Long id);

}

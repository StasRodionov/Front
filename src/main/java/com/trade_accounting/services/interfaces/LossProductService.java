package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.LossProductDto;

import java.util.List;

public interface LossProductService {
    List<LossProductDto> getAll();

    LossProductDto getById(Long id);

    LossProductDto create(LossProductDto lossProductDto);

    void update(LossProductDto lossProductDto);

    void deleteById(Long id);

}

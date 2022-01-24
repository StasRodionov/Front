package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.MovementProductDto;

import java.util.List;

public interface MovementProductService {
    List<MovementProductDto> getAll();

    MovementProductDto getById(Long id);

    MovementProductDto create(MovementProductDto movementProductDto);

    void update(MovementProductDto movementProductDto);

    void deleteById(Long id);
}

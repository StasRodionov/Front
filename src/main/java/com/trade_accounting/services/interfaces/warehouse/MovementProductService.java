package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.MovementProductDto;

import java.util.List;

public interface MovementProductService {
    List<MovementProductDto> getAll();

    MovementProductDto getById(Long id);

    MovementProductDto create(MovementProductDto movementProductDto);

    void update(MovementProductDto movementProductDto);

    void deleteById(Long id);
}

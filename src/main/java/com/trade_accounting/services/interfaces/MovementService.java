package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.MovementDto;

import java.util.List;

public interface MovementService {
    List<MovementDto> getAll();

    MovementDto getById(Long id);

    MovementDto create(MovementDto movementDto);

    void update(MovementDto movementDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

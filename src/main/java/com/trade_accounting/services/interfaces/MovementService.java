package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.models.dto.WarehouseDto;

import java.util.List;

public interface MovementService {
    List<MovementDto> getAll();

    MovementDto getById(Long id);

    MovementDto create(MovementDto movementDto);

    void update(MovementDto movementDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

    void updateTorg13(MovementDto movementDto, CompanyDto companyDto, WarehouseDto warehouseDto);
}

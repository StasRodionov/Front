package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.MovementDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;

import java.util.List;
import java.util.Map;

public interface MovementService {
    List<MovementDto> getAll();

    MovementDto getById(Long id);

    MovementDto create(MovementDto movementDto);

    List<MovementDto> searchByFilter(Map<String, String> queryMovement);
    List<MovementDto> searchByBetweenDataFilter(Map<String, String> queryMovement);

    void update(MovementDto movementDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

    void updateTorg13(MovementDto movementDto, CompanyDto companyDto, WarehouseDto warehouseDto);
}

package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.WarehouseDto;

import java.util.List;
import java.util.Map;

public interface WarehouseService {

    List<WarehouseDto> getAll();

    WarehouseDto getById(Long id);

    void create(WarehouseDto warehouseDto);

    void update(WarehouseDto warehouseDto);

    void deleteById(Long id);

    List<WarehouseDto> findBySearch(String search);

    List<WarehouseDto> search(Map<String, String> query);
}

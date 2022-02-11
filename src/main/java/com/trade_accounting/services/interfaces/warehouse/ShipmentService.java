package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ShipmentDto;

import java.util.List;

public interface ShipmentService {
    List<ShipmentDto> getAll();

    ShipmentDto getById(Long id);

    ShipmentDto create(ShipmentDto shipmentDto);

    void update(ShipmentDto shipmentDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

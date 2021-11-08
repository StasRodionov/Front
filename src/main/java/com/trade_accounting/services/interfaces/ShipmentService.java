package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ShipmentDto;
import com.trade_accounting.models.dto.ShipmentProductDto;

import java.util.List;

public interface ShipmentService {
    List<ShipmentDto> getAll();

    ShipmentDto getById(Long id);

    ShipmentDto create(ShipmentDto shipmentDto);

    void update(ShipmentDto shipmentDto);

    void deleteById(Long id);
}

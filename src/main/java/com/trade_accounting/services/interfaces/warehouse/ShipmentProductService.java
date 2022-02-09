package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ShipmentProductDto;

import java.util.List;

public interface ShipmentProductService {
    List<ShipmentProductDto> getAll();

    ShipmentProductDto getById(Long id);

    ShipmentProductDto create(ShipmentProductDto shipmentProductDto);

    void update(ShipmentProductDto shipmentProductDto);

    void deleteById(Long id);
}

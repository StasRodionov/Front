package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ShipmentProductDto;

import java.util.List;

public interface ShipmentProductService {
    List<ShipmentProductDto> getAll();

    ShipmentProductDto getById(Long id);

    ShipmentProductDto create(ShipmentProductDto shipmentProductDto);

    void update(ShipmentProductDto shipmentProductDto);

    void deleteById(Long id);
}

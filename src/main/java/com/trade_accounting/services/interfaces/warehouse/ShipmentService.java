package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ShipmentDto;

import java.util.List;
import java.util.Map;

public interface ShipmentService {
    List<ShipmentDto> getAll();

    List<ShipmentDto> getAll(String typeOfInvoice);

    ShipmentDto getById(Long id);

    ShipmentDto create(ShipmentDto shipmentDto);

    void update(ShipmentDto shipmentDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

    List<ShipmentDto> searchByFilter(Map<String,String> queryShipment);

    List<ShipmentDto> searchByString(String nameFilter);
}

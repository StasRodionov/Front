package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ShipmentDto;
import com.trade_accounting.models.dto.ShipmentProductDto;
import com.trade_accounting.services.interfaces.ShipmentService;
import com.trade_accounting.services.interfaces.api.ShipmentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentApi shipmentApi;
    private final String shipmentUrl;
    private final CallExecuteService<ShipmentDto> callExecuteService;
    private ShipmentDto shipmentDto;

    public ShipmentServiceImpl(Retrofit retrofit, @Value("${shipment_url}") String shipmentUrl, CallExecuteService<ShipmentDto> callExecuteService) {
        shipmentApi = retrofit.create(ShipmentApi.class);
        this.shipmentUrl = shipmentUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<ShipmentDto> getAll() {
        Call<List<ShipmentDto>> shipmentDtoListCall = shipmentApi.getAll(shipmentUrl);
        return callExecuteService.callExecuteBodyList(shipmentDtoListCall, ShipmentDto.class);
    }

    @Override
    public ShipmentDto getById(Long id) {
        Call<ShipmentDto> shipmentDtoCall = shipmentApi.getById(shipmentUrl, id);
        return callExecuteService.callExecuteBodyById(shipmentDtoCall, shipmentDto, ShipmentDto.class, id);
    }

    @Override
    public ShipmentDto create(ShipmentDto shipmentDto) {
        return null;
    }

    @Override
    public void update(ShipmentDto shipmentDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}

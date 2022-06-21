package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.warehouse.ShipmentProductDto;
import com.trade_accounting.services.interfaces.warehouse.ShipmentProductService;

import com.trade_accounting.services.api.warehouse.ShipmentProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class ShipmentProductServiceImpl implements ShipmentProductService {
    private final ShipmentProductApi shipmentProductApi;
    private final String shipmentProductUrl;
    private final CallExecuteService<ShipmentProductDto> callExecuteService;

    public ShipmentProductServiceImpl(Retrofit retrofit, @Value("${shipment_product_url}") String shipmentProductUrl, CallExecuteService<ShipmentProductDto> callExecuteService) {
        shipmentProductApi = retrofit.create(ShipmentProductApi.class);
        this.shipmentProductUrl = shipmentProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<ShipmentProductDto> getAll() {
        Call<List<ShipmentProductDto>> shipmentProductDtoList = shipmentProductApi.getAll(shipmentProductUrl);
        return callExecuteService.callExecuteBodyList(shipmentProductDtoList, ShipmentProductDto.class);
    }

    @Override
    public ShipmentProductDto getById(Long id) {
        Call<ShipmentProductDto> shipmentProductDtoCall = shipmentProductApi.getById(shipmentProductUrl, id);
        return callExecuteService.callExecuteBodyById(shipmentProductDtoCall, ShipmentProductDto.class, id);
    }

    @Override
    public ShipmentProductDto create(ShipmentProductDto shipmentProductDto) {
        return null;
    }

    @Override
    public void update(ShipmentProductDto shipmentProductDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

}

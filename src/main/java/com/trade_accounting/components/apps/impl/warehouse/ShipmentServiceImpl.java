package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.warehouse.ShipmentDto;
import com.trade_accounting.services.interfaces.warehouse.ShipmentService;
import com.trade_accounting.services.api.warehouse.ShipmentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentApi shipmentApi;
    private final String shipmentUrl;
    private final CallExecuteService<ShipmentDto> callExecuteService;

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
    public List<ShipmentDto> getAll(String typeOfInvoice) {
        List<ShipmentDto> invoiceDtoList = new ArrayList<>();
        Call<List<ShipmentDto>> invoiceDtoListCall = shipmentApi.getAll(shipmentUrl, typeOfInvoice);
        try {
            invoiceDtoList.addAll(Objects.requireNonNull(invoiceDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка ShipmentDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /purchases не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка ShipmentDto - {IOException}", e);
        }
        return invoiceDtoList;

    }
    @Override
    public ShipmentDto getById(Long id) {
        Call<ShipmentDto> shipmentDtoCall = shipmentApi.getById(shipmentUrl, id);
        return callExecuteService.callExecuteBodyById(shipmentDtoCall, ShipmentDto.class, id);
    }

    @Override
    public void create(ShipmentDto shipmentDto) {
        Call<Void> shipmentDtoCall = shipmentApi.create(shipmentUrl, shipmentDto);
        callExecuteService.callExecuteBodyCreate(shipmentDtoCall, ShipmentDto.class);
    }

    @Override
    public void update(ShipmentDto shipmentDto) {
        Call<Void> shipmentDtoCall = shipmentApi.update(shipmentUrl, shipmentDto);
        callExecuteService.callExecuteBodyUpdate(shipmentDtoCall, ShipmentDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> shipmentDtoCall = shipmentApi.deleteById(shipmentUrl, id);
        callExecuteService.callExecuteBodyDelete(shipmentDtoCall, ShipmentDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = shipmentApi.moveToIsRecyclebin(shipmentUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, ShipmentDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = shipmentApi.restoreFromIsRecyclebin(shipmentUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, ShipmentDto.class, id);

    }

    @Override
    public List<ShipmentDto> searchByFilter(Map<String, String> queryShipment) {
        List<ShipmentDto> shipmentDtoList = new ArrayList<>();
        Call<List<ShipmentDto>> callShipment = shipmentApi.searchByFilter(shipmentUrl,queryShipment);
        try {
            shipmentDtoList = callShipment.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение отгрузки по фильтру {}",queryShipment);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение отгрузки {IOException}", e);
        }
        return shipmentDtoList;
    }

    @Override
    public List<ShipmentDto> searchByString(String nameFilter) {
        List<ShipmentDto> shipmentDtoList = new ArrayList<>();
        Call<List<ShipmentDto>> getShipmentByNameFilter = shipmentApi.searchByString(shipmentUrl, nameFilter);
        try {
            shipmentDtoList = getShipmentByNameFilter.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение отгрузки по фильтру {}", nameFilter);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение отгрузки {IOException}", e);
        }
        return shipmentDtoList;
    }
}

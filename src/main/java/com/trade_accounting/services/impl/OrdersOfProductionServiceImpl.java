package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.OrdersOfProductionDto;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.services.interfaces.OrdersOfProductionService;
import com.trade_accounting.services.interfaces.api.OrdersOfProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrdersOfProductionServiceImpl implements OrdersOfProductionService {

    private final String ordersOfProductionUrl;
    private final OrdersOfProductionApi ordersOfProductionApi;
    private final OrdersOfProductionDto ordersOfProductionDto = new OrdersOfProductionDto();
    private final CallExecuteService<OrdersOfProductionDto> dtoCallExecuteService;

    public OrdersOfProductionServiceImpl(@Value("${orders_of_production_url}") String ordersOfProductionUrl, Retrofit retrofit, CallExecuteService<OrdersOfProductionDto> dtoCallExecuteService) {
        this.ordersOfProductionUrl = ordersOfProductionUrl;
        this.ordersOfProductionApi = retrofit.create(OrdersOfProductionApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<OrdersOfProductionDto> getAll() {
        Call<List<OrdersOfProductionDto>> ordersOfProductionGetAll = ordersOfProductionApi.getAll(ordersOfProductionUrl);
        return dtoCallExecuteService.callExecuteBodyList(ordersOfProductionGetAll, OrdersOfProductionDto.class);
    }

    @Override
    public OrdersOfProductionDto getById(Long id) {
        Call<OrdersOfProductionDto> ordersOfProductionDtoGetCall = ordersOfProductionApi.getById(ordersOfProductionUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(ordersOfProductionDtoGetCall, ordersOfProductionDto, OrdersOfProductionDto.class, id);
    }

    @Override
    public void create(OrdersOfProductionDto dto) {
        Call<Void> ordersOfProductionCreateCall = ordersOfProductionApi.create(ordersOfProductionUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(ordersOfProductionCreateCall, OrdersOfProductionDto.class);
    }

    @Override
    public void update(OrdersOfProductionDto dto) {
        Call<Void> ordersOfProductionUpdateCall = ordersOfProductionApi.update(ordersOfProductionUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(ordersOfProductionUpdateCall, OrdersOfProductionDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> ordersOfProductionDeleteCall = ordersOfProductionApi.deleteById(ordersOfProductionUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(ordersOfProductionDeleteCall, OrdersOfProductionDto.class, id);
    }
    @Override
    public List<OrdersOfProductionDto> searchOrdersOfProduction(Map<String, String> queryOrdersOfProduction) {
        List<OrdersOfProductionDto> ordersOfProductionList = new ArrayList<>();
        Call<List<OrdersOfProductionDto>> ordersOfProductionListCall = ordersOfProductionApi.searchOrdersOfProduction(ordersOfProductionUrl, queryOrdersOfProduction);
        try {
            ordersOfProductionList = ordersOfProductionListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка технических карт");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка технических карт: {IOException}", e);
        }
        return ordersOfProductionList;
    }
}

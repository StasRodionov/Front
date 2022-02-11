package com.trade_accounting.services.interfaces.production;


import com.trade_accounting.models.dto.production.OrdersOfProductionDto;

import java.util.List;
import java.util.Map;

public interface OrdersOfProductionService {

    List<OrdersOfProductionDto> getAll();

    OrdersOfProductionDto getById(Long id);

    void create(OrdersOfProductionDto dto);

    void update(OrdersOfProductionDto dto);

    void deleteById(Long id);

    List<OrdersOfProductionDto> searchOrdersOfProduction(Map<String, String> queryOrdersOfProduction);

    List<OrdersOfProductionDto> search(String query);

}

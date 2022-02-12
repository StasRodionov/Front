package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.SalesSubGoodsForSaleDto;

import java.util.List;
import java.util.Map;

public interface SalesSubGoodsForSaleService {

    List<SalesSubGoodsForSaleDto> getAll();

    void update(SalesSubGoodsForSaleDto salesSubGoodsForSaleDto);

    List<SalesSubGoodsForSaleDto> searchByFilter(Map<String ,String> query);
}


package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.SalesSubGoodsForSaleDto;

import java.util.List;
import java.util.Map;

public interface SalesSubGoodsForSaleService {

    List<SalesSubGoodsForSaleDto> getAll();

    void update(SalesSubGoodsForSaleDto salesSubGoodsForSaleDto);

    List<SalesSubGoodsForSaleDto> searchByFilter(Map<String ,String> query);
}


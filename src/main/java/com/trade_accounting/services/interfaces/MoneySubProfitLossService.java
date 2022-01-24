package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.MoneySubProfitLossDto;

import java.util.List;
import java.util.Map;

public interface MoneySubProfitLossService {
    List<MoneySubProfitLossDto> getAll();

    void update(MoneySubProfitLossDto moneySubProfitLossDto);

    List<MoneySubProfitLossDto> filter(Map<String, String> query);
}
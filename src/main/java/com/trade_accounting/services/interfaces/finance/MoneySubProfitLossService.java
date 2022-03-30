package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MoneySubProfitLossService {
    MoneySubProfitLossDto getAll();

    void update(MoneySubProfitLossDto moneySubProfitLossDto);

    //List<MoneySubProfitLossDto> filter(Map<String, String> query);

    MoneySubProfitLossDto filter(LocalDate startDatePeriod, LocalDate endDatePeriod, Long companyId);
}
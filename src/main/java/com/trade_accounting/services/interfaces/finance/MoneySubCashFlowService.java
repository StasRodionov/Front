package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.MoneySubCashFlowDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MoneySubCashFlowService {

    List<MoneySubCashFlowDto> getAll();

    void update(MoneySubCashFlowDto moneySubCashFlowDto);

    List<MoneySubCashFlowDto> filter(LocalDate startDatePeriod, LocalDate endDatePeriod, Long projectId, Long companyId, Long contractorId);
}

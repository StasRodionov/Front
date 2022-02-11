package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.MoneySubCashFlowDto;
import com.trade_accounting.services.interfaces.MoneySubCashFlowService;
import com.trade_accounting.services.interfaces.api.MoneySubCashFlowApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MoneySubCashFlowServiceImpl implements MoneySubCashFlowService {

    private final MoneySubCashFlowApi moneySubCashFlowApi;

    private final String moneySubCashFlowUrl;

    private final CallExecuteService<MoneySubCashFlowDto> dtoCallExecuteService;
    public MoneySubCashFlowServiceImpl(@Value("${moneySubCashFlowUrl_url}") String moneySubCashFlowUrl, Retrofit retrofit, CallExecuteService<MoneySubCashFlowDto> dtoCallExecuteService) {
        moneySubCashFlowApi = retrofit.create(MoneySubCashFlowApi.class);
        this.moneySubCashFlowUrl = moneySubCashFlowUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<MoneySubCashFlowDto> getAll() {
        Call<List<MoneySubCashFlowDto>> paymentDtoListCall = moneySubCashFlowApi.getAll(moneySubCashFlowUrl);
        return dtoCallExecuteService.callExecuteBodyList(paymentDtoListCall, MoneySubCashFlowDto.class);
    }

    @Override
    public void update(MoneySubCashFlowDto moneySubCashFlowDto) {

    }

    @Override
    public List<MoneySubCashFlowDto> filter(LocalDate startDatePeriod, LocalDate endDatePeriod, Long projectId, Long companyId, Long contractorId) {
        Call<List<MoneySubCashFlowDto>> paymentDtoListCall = moneySubCashFlowApi.filter(moneySubCashFlowUrl, startDatePeriod, endDatePeriod, projectId, companyId, contractorId);
        return dtoCallExecuteService.callExecuteBodyList(paymentDtoListCall, MoneySubCashFlowDto.class);
    }
}

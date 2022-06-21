package com.trade_accounting.components.apps.impl.finance;

import com.trade_accounting.models.dto.finance.MoneySubCashFlowDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.MoneySubCashFlowService;
import com.trade_accounting.services.api.finance.MoneySubCashFlowApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.time.LocalDate;
import java.util.List;

@Service
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

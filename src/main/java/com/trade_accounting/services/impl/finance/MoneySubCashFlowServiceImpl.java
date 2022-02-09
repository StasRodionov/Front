package com.trade_accounting.services.impl.finance;

import com.trade_accounting.models.dto.finance.MoneySubCashFlowDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.MoneySubCashFlowService;
import com.trade_accounting.services.api.finance.MoneySubCashFlowApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;

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
    public List<MoneySubCashFlowDto> filter(Map<String, String> query) {
        return null;
    }
}

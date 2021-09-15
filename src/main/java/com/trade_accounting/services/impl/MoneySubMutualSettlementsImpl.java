package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.MoneySubMutualSettlementsDto;
import com.trade_accounting.services.interfaces.MoneySubMutualSettlementsService;
import com.trade_accounting.services.interfaces.api.MoneySubMutualSettlementsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MoneySubMutualSettlementsImpl implements MoneySubMutualSettlementsService {

    private final MoneySubMutualSettlementsApi moneySubMutualSettlementsApi;

    private final String moneySubMutualSettlementsUrl;

    private final CallExecuteService<MoneySubMutualSettlementsDto> dtoCallExecuteService;

    public MoneySubMutualSettlementsImpl(@Value("${moneySubMutualSettlementsUrl_url}") String moneySubMutualSettlementsUrl, Retrofit retrofit, CallExecuteService<MoneySubMutualSettlementsDto> dtoCallExecuteService) {
        moneySubMutualSettlementsApi = retrofit.create(MoneySubMutualSettlementsApi.class);
        this.moneySubMutualSettlementsUrl = moneySubMutualSettlementsUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<MoneySubMutualSettlementsDto> getAll() {
        Call<List<MoneySubMutualSettlementsDto>> moneySubMutualSettlementsListCall = moneySubMutualSettlementsApi.getAll(moneySubMutualSettlementsUrl);
        return dtoCallExecuteService.callExecuteBodyList(moneySubMutualSettlementsListCall, MoneySubMutualSettlementsDto.class);
    }

    @Override
    public void update(MoneySubMutualSettlementsDto moneySubMutualSettlementsDto) {

    }

    @Override
    public List<MoneySubMutualSettlementsDto> filter(Map<String, String> query) {
        return null;
    }

}

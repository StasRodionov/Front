package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.MoneySubMutualSettlementsDto;
import com.trade_accounting.services.interfaces.MoneySubMutualSettlementsService;
import com.trade_accounting.services.interfaces.api.MoneySubMutualSettlementsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
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
    public List<MoneySubMutualSettlementsDto> searchByFilter(Map<String, String> query) {
        List<MoneySubMutualSettlementsDto> dataList = new ArrayList<>();
        Call<List<MoneySubMutualSettlementsDto>> callDataList = moneySubMutualSettlementsApi.searchByFilter(moneySubMutualSettlementsUrl, query);
        try{
            dataList = callDataList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка MoneySubMutualSettlementsDto по ФИЛЬТРУ -{}", query);
        }catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка MoneySubMutualSettlementsDto - ", e);
        }

        return dataList;
    }

}

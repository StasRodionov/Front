package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.SalesSubGoodsForSaleDto;
import com.trade_accounting.services.interfaces.SalesSubGoodsForSaleService;
import com.trade_accounting.services.interfaces.api.SalesSubGoodsForSaleApi;
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
public class SalesSubGoodsForSaleImpl implements SalesSubGoodsForSaleService {

    private final SalesSubGoodsForSaleApi salesSubGoodsForSaleApi;

    private final String salesSubGoodsForSaleUrl;

    private final CallExecuteService<SalesSubGoodsForSaleDto> dtoCallExecuteService;

    public SalesSubGoodsForSaleImpl(@Value("${salesSubGoodsForSaleUrl_url}") String salesSubGoodsForSaleUrl, Retrofit retrofit, CallExecuteService<SalesSubGoodsForSaleDto> dtoCallExecuteService) {
        salesSubGoodsForSaleApi = retrofit.create(SalesSubGoodsForSaleApi.class);
        this.salesSubGoodsForSaleUrl = salesSubGoodsForSaleUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<SalesSubGoodsForSaleDto> getAll() {
        Call<List<SalesSubGoodsForSaleDto>> salesSubGoodsForSaleListCall = salesSubGoodsForSaleApi.getAll(salesSubGoodsForSaleUrl);
        return dtoCallExecuteService.callExecuteBodyList(salesSubGoodsForSaleListCall, SalesSubGoodsForSaleDto.class);
    }

    @Override
    public void update(SalesSubGoodsForSaleDto salesSubGoodsForSaleDto) {

    }

    @Override
    public List<SalesSubGoodsForSaleDto> searchByFilter(Map<String, String> query) {
        List<SalesSubGoodsForSaleDto> dataList = new ArrayList<>();
        Call<List<SalesSubGoodsForSaleDto>> callDataList = salesSubGoodsForSaleApi.searchByFilter(salesSubGoodsForSaleUrl, query);
        try{
            dataList = callDataList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка SalesSubGoodsForSaleDto по ФИЛЬТРУ -{}", query);
        }catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка SalesSubGoodsForSaleDto - ", e);
        }

        return dataList;
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.api.BuyersReturnApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BuyersReturnServiceImpl implements BuyersReturnService {

    private final BuyersReturnApi buyersReturnApi;

    private final String buyersReturnUrl;

    private final CallExecuteService<BuyersReturnDto> dtoCallExecuteService;

    public BuyersReturnServiceImpl(@Value("${buyersReturn_url}") String buyersReturnUrl, Retrofit retrofit, CallExecuteService<BuyersReturnDto> dtoCallExecuteService) {
        buyersReturnApi = retrofit.create(BuyersReturnApi.class);
        this.buyersReturnUrl = buyersReturnUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<BuyersReturnDto> getByContractorId(Long id) {
        Call<List<BuyersReturnDto>> buyersReturnDtoListCall = buyersReturnApi.getByContractorId(buyersReturnUrl, id);
        return dtoCallExecuteService.callExecuteBodyList(buyersReturnDtoListCall, BuyersReturnDto.class);
    }

    @Override
    public List<BuyersReturnDto> searchByFilter(Map<String, String> query) {
        List<BuyersReturnDto> buyersReturnDtoList = new ArrayList<>();
        Call<List<BuyersReturnDto>> buyersReturnDtoCall = buyersReturnApi.searchByFilter(buyersReturnUrl, query);
        try {
            buyersReturnDtoList = buyersReturnDtoCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка BuyersReturnDto по ФИЛЬТРУ -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка BuyersReturnDto - ", e);
        }
        return buyersReturnDtoList;
    }

    @Override
    public List<BuyersReturnDto> getAll() {
        Call<List<BuyersReturnDto>> buyersReturnDtoListCall = buyersReturnApi.getAll(buyersReturnUrl);
        return dtoCallExecuteService.callExecuteBodyList(buyersReturnDtoListCall, BuyersReturnDto.class);
    }

    @Override
    public BuyersReturnDto getById(Long id) {
        Call<BuyersReturnDto> buyersReturnDtoCall = buyersReturnApi.getById(buyersReturnUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(buyersReturnDtoCall, BuyersReturnDto.class, id);
    }

    @Override
    public BuyersReturnDto create(BuyersReturnDto buyersReturnDto) {
        Call<BuyersReturnDto> buyersReturnDtoCall = buyersReturnApi.create(buyersReturnUrl, buyersReturnDto);

        try {
            buyersReturnDto = buyersReturnDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение BuyersReturnDto - {}", e);
        }

        return buyersReturnDto;
    }

    @Override
    public void update(BuyersReturnDto buyersReturnDto) {
        Call<Void> buyersReturnDtoCall = buyersReturnApi.update(buyersReturnUrl, buyersReturnDto);
        try {
            buyersReturnDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Movement");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра BuyersReturnDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> buyersReturnDtoCall = buyersReturnApi.deleteById(buyersReturnUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(buyersReturnDtoCall, BuyersReturnDto.class, id);
    }
    @Override
    public List<BuyersReturnDto> findBySearch(String search) {
        List<BuyersReturnDto> buyersReturnDtoList = new ArrayList<>();
        Call<List<BuyersReturnDto>> buyersReturnDtoListCall = buyersReturnApi
                .search(buyersReturnUrl, search.toLowerCase());

        try {
            buyersReturnDtoList = buyersReturnDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return buyersReturnDtoList;
    }

}

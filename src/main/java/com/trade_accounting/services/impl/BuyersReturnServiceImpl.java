package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.ContractDto;
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

    private BuyersReturnDto buyersReturnDto;

    private final CallExecuteService<BuyersReturnDto> dtoCallExecuteService;

    public BuyersReturnServiceImpl(@Value("${buyersReturn_url}") String buyersReturnUrl, Retrofit retrofit, CallExecuteService<BuyersReturnDto> dtoCallExecuteService) {
        buyersReturnApi = retrofit.create(BuyersReturnApi.class);
        this.buyersReturnUrl = buyersReturnUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<BuyersReturnDto> getAll() {
        Call<List<BuyersReturnDto>> buyersReturnDtoListCall = buyersReturnApi.getAll(buyersReturnUrl);
        return dtoCallExecuteService.callExecuteBodyList(buyersReturnDtoListCall, BuyersReturnDto.class);
    }

    @Override
    public BuyersReturnDto getById(Long id) {
        Call<BuyersReturnDto> buyersReturnDtoCall = buyersReturnApi.getById(buyersReturnUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(buyersReturnDtoCall, buyersReturnDto, BuyersReturnDto.class, id);
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
    public List<BuyersReturnDto> search(Map<String, String> query) {
        List<BuyersReturnDto> buyersReturnDtoList = new ArrayList<>();
        Call<List<BuyersReturnDto>> buyersReturnDtoListCall = buyersReturnApi.search(buyersReturnUrl, query);

        try {
            buyersReturnDtoList = buyersReturnDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка BuyersReturnDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка BuyersReturnDto - ", e);
        }
        return buyersReturnDtoList;
    }
}

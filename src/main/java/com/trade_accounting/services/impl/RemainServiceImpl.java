package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RemainDto;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.api.RemainApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RemainServiceImpl implements RemainService {

    private final RemainApi remainApi;
    private final String remainUrl;
    private final CallExecuteService<RemainDto> callExecuteService;

    public RemainServiceImpl(Retrofit retrofit, @Value("${remain_url}") String remainUrl, CallExecuteService<RemainDto> callExecuteService) {
        remainApi = retrofit.create(RemainApi.class);
        this.remainUrl = remainUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<RemainDto> getAll() {
        Call<List<RemainDto>> remainDtoListCall = remainApi.getAll(remainUrl);
        return callExecuteService.callExecuteBodyList(remainDtoListCall, RemainDto.class);
    }

    @Override
    public RemainDto getById(Long id) {
        Call<RemainDto> remainDtoCall = remainApi.getById(remainUrl, id);
        return callExecuteService.callExecuteBodyById(remainDtoCall, RemainDto.class, id);
    }

    @Override
    public RemainDto create(RemainDto remainDto) {
        Call<RemainDto> remainDtoCall = remainApi.create(remainUrl, remainDto);

        try {
            remainDto = remainDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание RemainDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение RemainDto - {}", e);
        }

        return remainDto;
    }

    @Override
    public void update(RemainDto remainDto) {
        Call<Void> remainDtoCall = remainApi.update(remainUrl, remainDto);
        try {
            remainDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Remain");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RemainDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> remainDtoCall = remainApi.deleteById(remainUrl, id);
        callExecuteService.callExecuteBodyDelete(remainDtoCall, RemainDto.class, id);
    }

    @Override
    public List<RemainDto> getAll(String typeOfRemain) {
        List<RemainDto> remainDtoList = new ArrayList<>();
        Call<List<RemainDto>> remainDtoListCall = remainApi.getAll(remainUrl, typeOfRemain);

        try {
            remainDtoList.addAll(Objects.requireNonNull(remainDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка RemainDto");
        } catch (IOException | NullPointerException e) {
            log.error("Попытка перехода на страницу /goods  не авторизованного пользователя  - {NullPointerException}", e);
            log.error("Произошла ошибка при выполнении запроса на получение списка RemainDto - {IOException}", e);
        }
        return remainDtoList;
    }

    @Override
    public List<RemainDto> findBySearchAndTypeOfRemain(String search, String typeOfRemain) {
        List<RemainDto> remainDtoList = new ArrayList<>();
        Call<List<RemainDto>> remainDtoListCall = remainApi
                .search(remainUrl, search.toLowerCase(), typeOfRemain);

        try {
            remainDtoList = remainDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов remain");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RemainDto - ", e);
        }
        return remainDtoList;
    }


}

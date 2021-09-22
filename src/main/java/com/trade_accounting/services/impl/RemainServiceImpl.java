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
import java.util.List;

@Service
@Slf4j
public class RemainServiceImpl implements RemainService {

    private final RemainApi remainApi;
    private final String remainUrl;
    private final CallExecuteService<RemainDto> callExecuteService;
    private RemainDto remainDto;

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
        return callExecuteService.callExecuteBodyById(remainDtoCall, remainDto, RemainDto.class, id);
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
}

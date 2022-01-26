package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PrepayoutDto;
import com.trade_accounting.services.interfaces.PrepayoutService;
import com.trade_accounting.services.interfaces.api.PrepayoutApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PrepayoutServiceImpl implements PrepayoutService {

    private final PrepayoutApi prepayoutApi;

    private final String prepayoutUrl;

    private final CallExecuteService<PrepayoutDto> dtoCallExecuteService;

    public PrepayoutServiceImpl(@Value("${prepayout_url}") String prepayoutUrl, Retrofit retrofit, CallExecuteService<PrepayoutDto> dtoCallExecuteService) {
        prepayoutApi = retrofit.create(PrepayoutApi.class);
        this.prepayoutUrl = prepayoutUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<PrepayoutDto> getAll() {
        Call<List<PrepayoutDto>> prepayoutDtoListCall = prepayoutApi.getAll(prepayoutUrl);
        return dtoCallExecuteService.callExecuteBodyList(prepayoutDtoListCall, PrepayoutDto.class);
    }

    @Override
    public PrepayoutDto getById(Long id) {
        Call<PrepayoutDto> prepayoutDtoCall = prepayoutApi.getById(prepayoutUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(prepayoutDtoCall, PrepayoutDto.class, id);
    }

    @Override
    public PrepayoutDto create(PrepayoutDto prepayoutDto) {
        Call<PrepayoutDto> prepayoutDtoCall = prepayoutApi.create(prepayoutUrl, prepayoutDto);

        try {
            prepayoutDto = prepayoutDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание Prepayout");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение PrepayoutDto - {}", e);
        }

        return prepayoutDto;
    }

    @Override
    public void update(PrepayoutDto prepayoutDto) {
        Call<Void> prepayoutDtoCall = prepayoutApi.update(prepayoutUrl, prepayoutDto);
        try {
            prepayoutDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Prepayout");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PrepayoutDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> prepayoutDtoCall = prepayoutApi.deleteById(prepayoutUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(prepayoutDtoCall, PrepayoutDto.class, id);
    }
}

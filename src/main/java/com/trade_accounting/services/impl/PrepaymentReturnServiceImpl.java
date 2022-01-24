package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.PrepaymentReturnDto;
import com.trade_accounting.services.interfaces.PrepaymentReturnService;
import com.trade_accounting.services.interfaces.api.PrepaymentReturnApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PrepaymentReturnServiceImpl implements PrepaymentReturnService {

    private final PrepaymentReturnApi prepaymentReturnApi;

    private final String prepaymentReturnUrl;

    private final CallExecuteService<PrepaymentReturnDto> dtoCallExecuteService;

    public PrepaymentReturnServiceImpl(@Value("${prepayment_return_url}") String prepaymentReturnUrl, Retrofit retrofit, CallExecuteService<PrepaymentReturnDto> dtoCallExecuteService) {
        prepaymentReturnApi = retrofit.create(PrepaymentReturnApi.class);
        this.prepaymentReturnUrl = prepaymentReturnUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<PrepaymentReturnDto> getAll() {
        Call<List<PrepaymentReturnDto>> prepaymentReturnApiAll = prepaymentReturnApi.getAll(prepaymentReturnUrl);
        return dtoCallExecuteService.callExecuteBodyList(prepaymentReturnApiAll, PrepaymentReturnDto.class);
    }

    @Override
    public PrepaymentReturnDto getById(Long id) {
        Call<PrepaymentReturnDto> prepaymentReturnDtoCall = prepaymentReturnApi.getById(prepaymentReturnUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(prepaymentReturnDtoCall, PrepaymentReturnDto.class, id);
    }

    @Override
    public PrepaymentReturnDto create(PrepaymentReturnDto prepaymentReturnDto) {
        Call<PrepaymentReturnDto> prepaymentReturnDtoCall = prepaymentReturnApi.create(prepaymentReturnUrl, prepaymentReturnDto);

        try {
            prepaymentReturnDto = prepaymentReturnDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание Prepayout");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение PrepayoutDto - {}", e);
        }

        return prepaymentReturnDto;
    }

    @Override
    public void update(PrepaymentReturnDto prepaymentReturnDto) {
        Call<Void> prepaymentReturnDtoCall = prepaymentReturnApi.update(prepaymentReturnUrl, prepaymentReturnDto);
        try {
            prepaymentReturnDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Prepayout");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PrepayoutDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> prepamentReturnDtoCall = prepaymentReturnApi.deleteById(prepaymentReturnUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(prepamentReturnDtoCall, PrepaymentReturnDto.class, id);
    }
}

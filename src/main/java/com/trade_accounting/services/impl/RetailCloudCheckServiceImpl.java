package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.RetailCloudCheckDto;
import com.trade_accounting.services.interfaces.RetailCloudCheckService;
import com.trade_accounting.services.interfaces.api.RetailCloudCheckApi;
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
public class RetailCloudCheckServiceImpl implements RetailCloudCheckService {

    private final RetailCloudCheckApi retailCloudCheckApi;

    private final String retailCloudCheckUrl;

    private final CallExecuteService<RetailCloudCheckDto> dtoCallExecuteService;

    private RetailCloudCheckDto retailCloudCheckDto;

    public RetailCloudCheckServiceImpl(@Value("${retail_cloud_check_url}") String retailCloudCheckUrl,
                                       CallExecuteService<RetailCloudCheckDto> dtoCallExecuteService,
                                       Retrofit retrofit) {
        this.retailCloudCheckApi = retrofit.create(RetailCloudCheckApi.class);
        this.retailCloudCheckUrl = retailCloudCheckUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailCloudCheckDto> getAll() {
        Call<List<RetailCloudCheckDto>> retailCloudCheckList = retailCloudCheckApi.getAll(retailCloudCheckUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailCloudCheckList, RetailCloudCheckDto.class);
    }

    @Override
    public RetailCloudCheckDto getById(Long id) {
        Call<RetailCloudCheckDto> retailCloudCheckDtoCall = retailCloudCheckApi.getById(retailCloudCheckUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailCloudCheckDtoCall, RetailCloudCheckDto.class, id);
    }

    @Override
    public List<RetailCloudCheckDto> search(Map<String, String> query) {
        List<RetailCloudCheckDto> retailCloudCheckDtoList = new ArrayList<>();
        Call<List<RetailCloudCheckDto>> retailCloudCheckDtoListCall = retailCloudCheckApi.search(retailCloudCheckUrl, query);
        try {
            retailCloudCheckDtoList = retailCloudCheckDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка счетов invoice -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка InvoiceDto - ", e);
        }
        return retailCloudCheckDtoList;
    }



    @Override
    public void create(RetailCloudCheckDto retailCloudCheckDto) {
        Call<Void> retailCloudCheckCall = retailCloudCheckApi.create(retailCloudCheckUrl, retailCloudCheckDto);

        try {
            retailCloudCheckCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра RetailMakingDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра RetailMakingDto - {}", e);
        }
    }

    @Override
    public void update(RetailCloudCheckDto retailCloudCheckDto) {
        Call<Void> retailCloudCheckCall = retailCloudCheckApi.create(retailCloudCheckUrl, retailCloudCheckDto);

        try {
            retailCloudCheckCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра RetailMakingDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RetailMakingDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailCloudCheckCall = retailCloudCheckApi.create(retailCloudCheckUrl, retailCloudCheckDto);

        try {
            retailCloudCheckCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра RetailMakingDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра RetailMakingDto по id= {} - {}", id, e);
        }
    }
}

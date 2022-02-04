package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailMakingDto;
import com.trade_accounting.services.interfaces.RetailMakingService;
import com.trade_accounting.services.interfaces.api.RetailMakingApi;
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
public class RetailMakingServiceImpl implements RetailMakingService {

    private final RetailMakingApi retailMakingApi;

    private final String retailMakingUrl;

    private final CallExecuteService<RetailMakingDto> dtoCallExecuteService;

    public RetailMakingServiceImpl(@Value("${retail_making_url}") String retailMakingUrl, Retrofit retrofit,
                                   CallExecuteService<RetailMakingDto> dtoCallExecuteService) {
        retailMakingApi = retrofit.create(RetailMakingApi.class);
        this.retailMakingUrl = retailMakingUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }


    @Override
    public List<RetailMakingDto> getAll() {
        Call<List<RetailMakingDto>> retailMakingDtoList = retailMakingApi.getAll(retailMakingUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailMakingDtoList, RetailMakingDto.class);
    }

    @Override
    public RetailMakingDto getById(Long id) {
        Call<RetailMakingDto> retailMakingDtoCall = retailMakingApi.getById(retailMakingUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailMakingDtoCall, RetailMakingDto.class, id);
    }

    @Override
    public void create(RetailMakingDto retailMakingDto) {

        Call<Void> retailMakingDtoCall = retailMakingApi.create(retailMakingUrl, retailMakingDto);

        try {
            retailMakingDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра RetailMakingDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра RetailMakingDto - {}", e);
        }
    }

    @Override
    public void update(RetailMakingDto retailMakingDto) {

        Call<Void> retailMakingDtoCall = retailMakingApi.update(retailMakingUrl, retailMakingDto);

        try {
            retailMakingDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра RetailMakingDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RetailMakingDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> retailMakingDtoCall = retailMakingApi.deleteById(retailMakingUrl, id);

        try {
            retailMakingDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра RetailMakingDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра RetailMakingDto по id= {} - {}", id, e);
        }
    }

    @Override
    public List<RetailMakingDto> searchByFilter(Map<String, String> queryRetailMaking) {
        List<RetailMakingDto> retailMakingDtoList = new ArrayList<>();
        Call<List<RetailMakingDto>> callListRetailMaking = retailMakingApi.searchByFilter(retailMakingUrl, queryRetailMaking);
        try {
            retailMakingDtoList = callListRetailMaking.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение внесений по фильтру {}", queryRetailMaking);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение внесений по фильтру {IOException}", e);
        }
        return retailMakingDtoList;
    }

    @Override
    public List<RetailMakingDto> search(String search) {
        List<RetailMakingDto> retailMakingDtoList = new ArrayList<>();
        Call<List<RetailMakingDto>> callListRetailMaking = retailMakingApi.search(retailMakingUrl, search);
        try {
            retailMakingDtoList = callListRetailMaking.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение внесений по быстрому поиску {}", search);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение внесений по быстрому поиску {IOException}", e);
        }
        return retailMakingDtoList;
    }

}

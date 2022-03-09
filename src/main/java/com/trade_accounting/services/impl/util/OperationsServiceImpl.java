package com.trade_accounting.services.impl.util;

import com.trade_accounting.models.dto.util.OperationsDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.util.OperationsService;
import com.trade_accounting.services.api.util.OperationsApi;
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
public class OperationsServiceImpl implements OperationsService {

    private final OperationsApi operationsApi;
    private final String operationsUrl;
    private final CallExecuteService<OperationsDto> callExecuteService;

    public OperationsServiceImpl(Retrofit retrofit, @Value("${operations_url}") String operationsUrl, CallExecuteService<OperationsDto> callExecuteService) {
        this.operationsApi = retrofit.create(OperationsApi.class);
        this.operationsUrl = operationsUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<OperationsDto> getAll() {
        Call<List<OperationsDto>> operationsDtoListCall = operationsApi.getAll(operationsUrl);
        return callExecuteService.callExecuteBodyList(operationsDtoListCall, OperationsDto.class);
    }

    @Override
    public OperationsDto getById(Long id) {
        Call<OperationsDto> operationsDtoListCall = operationsApi.getById(operationsUrl, id);
        return callExecuteService.callExecuteBodyById(operationsDtoListCall, OperationsDto.class, id);
    }

    @Override
    public List<OperationsDto> searchByFilter(Map<String, String> queryOperations) {
        List<OperationsDto> operationsDtoList = new ArrayList<>();
        Call<List<OperationsDto>> callOperation = operationsApi.searchByFilter(operationsUrl, queryOperations);
        try {
            operationsDtoList = callOperation.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение документов по фильтру {}", operationsDtoList);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение документов {IOException}", e);
        }
        return operationsDtoList;
    }

    @Override
    public List<OperationsDto> quickSearch(String search) {
        List<OperationsDto> operationsDtoList = new ArrayList<>();
        Call<List<OperationsDto>> operationsDtoListCall = operationsApi
                .quickSearch(operationsUrl, search.toLowerCase());

        try {
            operationsDtoList = operationsDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка документов");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка документов: ", e);
        }
        return operationsDtoList;
    }

    @Override
    public List<OperationsDto> quickSearchRecycle(String search) {
        List<OperationsDto> operationsDtoList = new ArrayList<>();
        Call<List<OperationsDto>> operationsDtoListCall = operationsApi
                .quickSearch(operationsUrl, search.toLowerCase());

        try {
            operationsDtoList = operationsDtoListCall.execute().body();
            if (operationsDtoList != null) {
                operationsDtoList.removeIf(operationsDto -> !operationsDto.getIsRecyclebin());
            }
            log.info("Успешно выполнен запрос на поиск и получение списка документов в корзине");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка документов в корзине: ", e);
        }
        return operationsDtoList;
    }
}


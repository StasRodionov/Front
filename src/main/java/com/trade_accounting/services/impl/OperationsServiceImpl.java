package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.OperationsDto;
import com.trade_accounting.services.interfaces.OperationsService;
import com.trade_accounting.services.interfaces.api.OperationsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

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
}


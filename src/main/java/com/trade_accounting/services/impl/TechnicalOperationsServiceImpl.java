package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.api.TechnicalOperationsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TechnicalOperationsServiceImpl implements TechnicalOperationsService {

    private final String technicalOperationsUrl;
    private final TechnicalOperationsApi technicalOperationsApi;
    private TechnicalOperationsDto technicalOperationsDto = new TechnicalOperationsDto();
    private final CallExecuteService<TechnicalOperationsDto> dtoCallExecuteService;

    public TechnicalOperationsServiceImpl(@Value("${technical_operations_url}") String technicalOperationsUrl, Retrofit retrofit, CallExecuteService<TechnicalOperationsDto> dtoCallExecuteService) {
        this.technicalOperationsUrl = technicalOperationsUrl;
        this.technicalOperationsApi = retrofit.create(TechnicalOperationsApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalOperationsDto> getAll() {
        Call<List<TechnicalOperationsDto>> technicalOperationsGetAll = technicalOperationsApi.getAll(technicalOperationsUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalOperationsGetAll, TechnicalOperationsDto.class);
    }

    @Override
    public TechnicalOperationsDto getById(Long id) {
        Call<TechnicalOperationsDto> technicalOperationsDtoGetCall = technicalOperationsApi.getById(technicalOperationsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalOperationsDtoGetCall, technicalOperationsDto, TechnicalOperationsDto.class, id);
    }

    @Override
    public void create(TechnicalOperationsDto dto) {
        Call<Void> technicalOperationsCreateCall = technicalOperationsApi.create(technicalOperationsUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(technicalOperationsCreateCall, TechnicalOperationsDto.class);
    }

    @Override
    public void update(TechnicalOperationsDto dto) {
        Call<Void> technicalOperationsUpdateCall = technicalOperationsApi.update(technicalOperationsUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(technicalOperationsUpdateCall, TechnicalOperationsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> technicalOperationsDeleteCall = technicalOperationsApi.deleteById(technicalOperationsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(technicalOperationsDeleteCall, TechnicalOperationsDto.class, id);
    }
}

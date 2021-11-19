package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TechnicalCardGroupDto;
import com.trade_accounting.models.dto.TechnicalCardProductionDto;
import com.trade_accounting.services.interfaces.TechnicalCardProductionService;
import com.trade_accounting.services.interfaces.api.TechnicalCardGroupApi;
import com.trade_accounting.services.interfaces.api.TechnicalCardProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TechnicalCardProductionServiceImpl implements TechnicalCardProductionService {

    private final String technicalCardProductionUrl;
    private final TechnicalCardProductionApi technicalCardProductionApi;
    private final CallExecuteService<TechnicalCardProductionDto> dtoCallExecuteService;

    public TechnicalCardProductionServiceImpl(@Value("${technical_card_group_url}") String technicalCardGroupUrl, Retrofit retrofit, CallExecuteService<TechnicalCardProductionDto> dtoCallExecuteService) {
        this.technicalCardProductionUrl = technicalCardGroupUrl;
        this.technicalCardProductionApi = retrofit.create(TechnicalCardProductionApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalCardProductionDto> getAll() {
        Call<List<TechnicalCardProductionDto>> technicalCardProductionGetAll = technicalCardProductionApi.getAll(technicalCardProductionUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalCardProductionGetAll, TechnicalCardProductionDto.class);
    }

    @Override
    public TechnicalCardProductionDto getById(Long id) {
        Call<TechnicalCardProductionDto> technicalCardProductionDtoGetCall = technicalCardProductionApi.getById(technicalCardProductionUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalCardProductionDtoGetCall, TechnicalCardProductionDto.class, id);
    }

    @Override
    public void create(TechnicalCardProductionDto dto) {

    }

    @Override
    public void update(TechnicalCardProductionDto dto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}

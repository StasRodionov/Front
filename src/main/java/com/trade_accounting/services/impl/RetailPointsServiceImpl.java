package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailPointsDto;
import com.trade_accounting.services.interfaces.RetailPointsService;
import com.trade_accounting.services.interfaces.api.RetailPointsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class RetailPointsServiceImpl implements RetailPointsService {

    private final RetailPointsApi retailPointsApi;

    private final String retailPointsUrl;

    private final CallExecuteService<RetailPointsDto> dtoCallExecuteService;

    public RetailPointsServiceImpl(Retrofit retrofit, @Value("${retail_points_url}") String retailPointsUrl, CallExecuteService<RetailPointsDto> dtoCallExecuteService) {
        this.retailPointsApi = retrofit.create(RetailPointsApi.class);
        this.retailPointsUrl = retailPointsUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailPointsDto> getAll() {
        Call<List<RetailPointsDto>> retailPointsDtoListCall = retailPointsApi.getAll(retailPointsUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailPointsDtoListCall, RetailPointsDto.class);
    }

    @Override
    public RetailPointsDto getById(Long id) {
        Call<RetailPointsDto> retailPointsDtoCall = retailPointsApi.getById(retailPointsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailPointsDtoCall, RetailPointsDto.class, id);
    }

    @Override
    public void create(RetailPointsDto retailSalesDto) {
        Call<Void> retailPointsDtoListCall = retailPointsApi.create(retailPointsUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailPointsDtoListCall, RetailPointsDto.class);

    }

    @Override
    public void update(RetailPointsDto retailSalesDto) {
        Call<Void> retailPointsDtoListCall = retailPointsApi.update(retailPointsUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailPointsDtoListCall, RetailPointsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailPointsDtoListCall = retailPointsApi.deleteById(retailPointsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailPointsDtoListCall, RetailPointsDto.class, id);
    }
}
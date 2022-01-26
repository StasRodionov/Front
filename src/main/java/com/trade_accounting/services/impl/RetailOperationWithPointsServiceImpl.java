package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailOperationWithPointsDto;
import com.trade_accounting.services.interfaces.RetailOperationWithPointsService;
import com.trade_accounting.services.interfaces.api.RetailOperationWithPointsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class RetailOperationWithPointsServiceImpl implements RetailOperationWithPointsService {

    private final RetailOperationWithPointsApi retailOperationWithPointsApi;

    private final String retailOperationWithPointsUrl;

    private final CallExecuteService<RetailOperationWithPointsDto> dtoCallExecuteService;

    public RetailOperationWithPointsServiceImpl(@Value("${retail_operation_with_points_url}")String retailOperationWithPointsUrl,
                                                Retrofit retrofit,
                                                CallExecuteService<RetailOperationWithPointsDto> dtoCallExecuteService) {
        this.retailOperationWithPointsUrl = retailOperationWithPointsUrl;
        retailOperationWithPointsApi = retrofit.create((RetailOperationWithPointsApi.class));
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailOperationWithPointsDto> getAll() {

        Call<List<RetailOperationWithPointsDto>> retailOperationWithPointsDtoCall = retailOperationWithPointsApi
                .getAll(retailOperationWithPointsUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailOperationWithPointsDtoCall, RetailOperationWithPointsDto.class);
    }

    @Override
    public RetailOperationWithPointsDto getById(Long id) {
        Call<RetailOperationWithPointsDto> retailOperationWithPointsDtoCall = retailOperationWithPointsApi
                .getById(retailOperationWithPointsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailOperationWithPointsDtoCall,
                RetailOperationWithPointsDto.class, id);
    }

    @Override
    public void create(RetailOperationWithPointsDto retailOperationWithPointsDto) {
        Call<Void> retailOperationWithPointsDtoCall = retailOperationWithPointsApi.create(retailOperationWithPointsUrl,
                retailOperationWithPointsDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailOperationWithPointsDtoCall, RetailOperationWithPointsDto.class);
    }

    @Override
    public void update(RetailOperationWithPointsDto retailOperationWithPointsDto) {
        Call<Void> retailOperationWithPointsDtoCall = retailOperationWithPointsApi.update(retailOperationWithPointsUrl,
                retailOperationWithPointsDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailOperationWithPointsDtoCall, RetailOperationWithPointsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailOperationWithPointsDtoCall = retailOperationWithPointsApi.deleteById(retailOperationWithPointsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailOperationWithPointsDtoCall, RetailOperationWithPointsDto.class, id);
    }
}

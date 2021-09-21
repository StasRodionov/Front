package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailReturnsDto;
import com.trade_accounting.services.interfaces.RetailReturnsService;
import com.trade_accounting.services.interfaces.api.RetailReturnsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;


@Slf4j
@Service
public class RetailReturnsServiceImpl implements RetailReturnsService {

    private final RetailReturnsApi retailReturnsApi;

    private final String retailReturnUrl;

    private RetailReturnsDto retailReturnsDto;

    private final CallExecuteService<RetailReturnsDto> dtoCallExecuteService;

    public RetailReturnsServiceImpl(Retrofit retrofit, @Value("${returns_url}") String retailReturnUrl, CallExecuteService<RetailReturnsDto> dtoCallExecuteService) {
        this.retailReturnsApi = retrofit.create(RetailReturnsApi.class);
        this.retailReturnUrl = retailReturnUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailReturnsDto> getAll() {
        Call<List<RetailReturnsDto>> retailReturnsDtoListCall = retailReturnsApi.getAll(retailReturnUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailReturnsDtoListCall, RetailReturnsDto.class);
    }

    @Override
    public RetailReturnsDto getById(Long id) {
        Call<RetailReturnsDto> retailReturnsDtoCall = retailReturnsApi.getById(retailReturnUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailReturnsDtoCall, retailReturnsDto, RetailReturnsDto.class, id);
    }

    @Override
    public void create(RetailReturnsDto retailSalesDto) {
        Call<Void> retailStoreDtoCall = retailReturnsApi.create(retailReturnUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailStoreDtoCall, RetailReturnsDto.class);

    }

    @Override
    public void update(RetailReturnsDto retailSalesDto) {
        Call<Void> retailReturnsDtoCall = retailReturnsApi.update(retailReturnUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailReturnsDtoCall, RetailReturnsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailReturnsDtoCall = retailReturnsApi.deleteById(retailReturnUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailReturnsDtoCall, RetailReturnsDto.class, id);
    }
}

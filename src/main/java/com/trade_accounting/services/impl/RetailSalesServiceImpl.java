package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailSalesDto;
import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.RetailSalesService;
import com.trade_accounting.services.interfaces.api.RetailSalesApi;
import com.trade_accounting.services.interfaces.api.RetailStoreApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RetailSalesServiceImpl implements RetailSalesService {

    private final RetailSalesApi retailSalesApi;

    private final String retailSalesUrl;

    private RetailSalesDto retailSalesDto;

    private final CallExecuteService<RetailSalesDto> dtoCallExecuteService;

    public RetailSalesServiceImpl(Retrofit retrofit, @Value("${retail_sales_url}") String retailSalesUrl, CallExecuteService<RetailSalesDto> dtoCallExecuteService) {
        this.retailSalesApi = retrofit.create(RetailSalesApi.class);
        this.retailSalesUrl = retailSalesUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailSalesDto> getAll() {
        Call<List<RetailSalesDto>> retailSalesDtoListCall = retailSalesApi.getAll(retailSalesUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailSalesDtoListCall, RetailSalesDto.class);
    }

    @Override
    public RetailSalesDto getById(Long id) {
        Call<RetailSalesDto> retailSalesDtoCall = retailSalesApi.getById(retailSalesUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailSalesDtoCall, retailSalesDto, RetailSalesDto.class, id);
    }

    @Override
    public void create(RetailSalesDto retailSalesDto) {
        Call<Void> retailStoreDtoCall = retailSalesApi.create(retailSalesUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailStoreDtoCall, RetailSalesDto.class);

    }

    @Override
    public void update(RetailSalesDto retailSalesDto) {
        Call<Void> retailSalesDtoCall = retailSalesApi.update(retailSalesUrl, retailSalesDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailSalesDtoCall, RetailSalesDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailSalesDtoCall = retailSalesApi.deleteById(retailSalesUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailSalesDtoCall, RetailSalesDto.class, id);
    }
}

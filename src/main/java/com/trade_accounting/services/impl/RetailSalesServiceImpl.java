package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailSalesDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.RetailSalesService;
import com.trade_accounting.services.interfaces.api.RetailSalesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RetailSalesServiceImpl implements RetailSalesService {

    private final RetailSalesApi retailSalesApi;

    private final String retailSalesUrl;

    private RetailSalesDto retailSalesDto;
    private List<RetailSalesDto> retailSalesDtoList = new ArrayList<>();

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

    @Override
    public List<RetailSalesDto> search(String query) {
        Call<List<RetailSalesDto>> retailSalesListCall = retailSalesApi.search(retailSalesUrl, query);
        try {
            retailSalesDtoList = retailSalesListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка TechnicalOperationsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка TechnicalOperationsDto - ", e);
        }
        return retailSalesDtoList;
    }

    @Override
    public List<RetailSalesDto> searchRetailSales(Map<String, String> queryRetailSales) {
        List<RetailSalesDto> retailSalesDtoList = new ArrayList<>();
        Call<List<RetailSalesDto>> retailSalesDtoListCall = retailSalesApi.searchContractor(retailSalesUrl, queryRetailSales);
        try {
            retailSalesDtoList = retailSalesDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка технических операции");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка технических операции: {IOException}", e);
        }
        return retailSalesDtoList;

    }
}

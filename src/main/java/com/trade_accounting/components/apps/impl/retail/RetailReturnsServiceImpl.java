package com.trade_accounting.components.apps.impl.retail;

import com.trade_accounting.models.dto.retail.RetailReturnsDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.retail.RetailReturnsService;
import com.trade_accounting.services.api.retail.RetailReturnsApi;
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
public class RetailReturnsServiceImpl implements RetailReturnsService {

    private final RetailReturnsApi retailReturnsApi;

    private final String retailReturnUrl;

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
        return dtoCallExecuteService.callExecuteBodyById(retailReturnsDtoCall, RetailReturnsDto.class, id);
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

    @Override
    public List<RetailReturnsDto> search(String query) {
        List<RetailReturnsDto> retailReturnsDtoList = new ArrayList<>();
        Call<List<RetailReturnsDto>> retailReturnsListCall = retailReturnsApi.search(retailReturnUrl, query);
        try {
            retailReturnsDtoList = retailReturnsListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка RetailReturnsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RetailReturnsDto - ", e);
        }
        return retailReturnsDtoList;
    }

    @Override
    public List<RetailReturnsDto> searchRetailReturns(Map<String, String> queryRetailReturns) {
        List<RetailReturnsDto> retailReturnsDtoList = new ArrayList<>();
        Call<List<RetailReturnsDto>> retailReturnsListCall = retailReturnsApi.searchContractor(retailReturnUrl, queryRetailReturns);
        try {
            retailReturnsDtoList = retailReturnsListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка RetailReturnsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RetailReturnsDto - ", e);
        }
        return retailReturnsDtoList;
    }
}

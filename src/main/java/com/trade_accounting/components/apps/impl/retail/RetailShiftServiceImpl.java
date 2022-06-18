package com.trade_accounting.components.apps.impl.retail;

import com.trade_accounting.models.dto.retail.RetailShiftDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.retail.RetailShiftService;
import com.trade_accounting.services.api.retail.RetailShiftApi;
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
public class RetailShiftServiceImpl implements RetailShiftService {

    private final RetailShiftApi retailShiftApi;

    private final String retailShiftUrl;

    private List<RetailShiftDto> retailShiftDtoList = new ArrayList<>();

    private final CallExecuteService<RetailShiftDto> dtoCallExecuteService;

    public RetailShiftServiceImpl(Retrofit retrofit, @Value("${retail_shift_url}") String retailShiftUrl, CallExecuteService<RetailShiftDto> dtoCallExecuteService) {
        this.retailShiftApi = retrofit.create(RetailShiftApi.class);
        this.retailShiftUrl = retailShiftUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailShiftDto> getAll() {
        Call<List<RetailShiftDto>> retailShiftApiAll = retailShiftApi.getAll(retailShiftUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailShiftApiAll, RetailShiftDto.class);
    }

    @Override
    public RetailShiftDto getById(Long id) {
        Call<RetailShiftDto> retailShiftDtoCall = retailShiftApi.getById(retailShiftUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailShiftDtoCall, RetailShiftDto.class, id);
    }

    @Override
    public void create(RetailShiftDto retailShiftDto) {
        Call<Void> retailShiftDtoCall = retailShiftApi.create(retailShiftUrl, retailShiftDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailShiftDtoCall, RetailShiftDto.class);

    }

    @Override
    public void update(RetailShiftDto retailShiftDto) {
        Call<Void> retailShiftDtoCall = retailShiftApi.update(retailShiftUrl, retailShiftDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailShiftDtoCall, RetailShiftDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailShiftDtoCall = retailShiftApi.deleteById(retailShiftUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailShiftDtoCall, RetailShiftDto.class, id);
    }

    @Override
    public List<RetailShiftDto> search(String query) {
        Call<List<RetailShiftDto>> retailShiftDtoListCall = retailShiftApi.search(retailShiftUrl, query);
        try {
            retailShiftDtoList = retailShiftDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка RetailShiftDto по быстрому поиску");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка RetailShiftDto по быстрому поиску - ", e);
        }
        return retailShiftDtoList;
    }

    @Override
    public List<RetailShiftDto> searchByFilter(Map<String, String> query) {
        List<RetailShiftDto> retailShiftDtoList = new ArrayList<>();
        Call<List<RetailShiftDto>> retailShiftDtoListCall = retailShiftApi.searchByFilter(retailShiftUrl, query);
        try {
            retailShiftDtoList = retailShiftDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение смен RetailShiftDto по фильтру {}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение смен RetailShiftDto по фильтру - ", e);
        }
        return retailShiftDtoList;
    }
}

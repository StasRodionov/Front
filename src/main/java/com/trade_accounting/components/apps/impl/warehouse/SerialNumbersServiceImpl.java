package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.warehouse.SerialNumbersDto;
import com.trade_accounting.services.api.warehouse.SerialNumbersApi;
import com.trade_accounting.services.interfaces.warehouse.SerialNumbersService;
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
public class SerialNumbersServiceImpl implements SerialNumbersService {

    private final SerialNumbersApi serialNumbersApi;

    private final String serialNumbersUrl;

    private final CallExecuteService<SerialNumbersDto> dtoCallExecuteService;

    public SerialNumbersServiceImpl(Retrofit retrofit, @Value("${serial_numbers_url}") String serialNumbersUrl,
                                    CallExecuteService<SerialNumbersDto> dtoCallExecuteService) {
        this.serialNumbersUrl = serialNumbersUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
        serialNumbersApi = retrofit.create(SerialNumbersApi.class);
    }

    @Override
    public List<SerialNumbersDto> getAll() {
        Call<List<SerialNumbersDto>> serialNumbersListCall = serialNumbersApi.getAll(serialNumbersUrl);
        return dtoCallExecuteService.callExecuteBodyList(serialNumbersListCall, SerialNumbersDto.class);
    }

    @Override
    public void update(SerialNumbersDto serialNumbersDto) {
        Call<Void> serialNumbersCall = serialNumbersApi.update(serialNumbersUrl, serialNumbersDto);
        dtoCallExecuteService.callExecuteBodyUpdate(serialNumbersCall, SerialNumbersDto.class);
    }

    @Override
    public List<SerialNumbersDto> searchByFilter(Map<String, String> query) {
        List<SerialNumbersDto> dataList = new ArrayList<>();
        Call<List<SerialNumbersDto>> callDataList = serialNumbersApi.searchByFilter(serialNumbersUrl, query);
        try{
            dataList = callDataList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка SalesSubGoodsForSaleDto по ФИЛЬТРУ -{}", query);
        }catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка SalesSubGoodsForSaleDto - ", e);
        }

        return dataList;
    }
}

package com.trade_accounting.components.apps.impl.warehouse;

import com.trade_accounting.models.dto.warehouse.AcceptanceProductionDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceProductionService;
import com.trade_accounting.services.api.warehouse.AcceptanceProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AcceptanceProductionServiceImpl implements AcceptanceProductionService {
    private final AcceptanceProductionApi acceptanceProductionApi;
    private final String acceptanceProductUrl;
    private final CallExecuteService<AcceptanceProductionDto> callExecuteService;
    private List<AcceptanceProductionDto> acceptanceProductionDtoList = new ArrayList<>();

    public AcceptanceProductionServiceImpl(Retrofit retrofit, @Value("${acceptance_product_url}") String acceptanceProductUrl,
                                           CallExecuteService<AcceptanceProductionDto> callExecuteService) {
        acceptanceProductionApi = retrofit.create(AcceptanceProductionApi.class);
        this.acceptanceProductUrl = acceptanceProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceProductionDto> getAll() {
        Call<List<AcceptanceProductionDto>> acceptanceProductionDtoCall = acceptanceProductionApi.getAll(acceptanceProductUrl);
        return callExecuteService.callExecuteBodyList(acceptanceProductionDtoCall, AcceptanceProductionDto.class);
    }

    @Override
    public AcceptanceProductionDto getById(Long id) {
        Call<AcceptanceProductionDto> acceptanceProductionDtoCall = acceptanceProductionApi.getById(acceptanceProductUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceProductionDtoCall, AcceptanceProductionDto.class, id);
    }

    @Override
    public Response<AcceptanceProductionDto> create(AcceptanceProductionDto acceptanceProductionDto) {
        Call<AcceptanceProductionDto> acceptanceDtoCall = acceptanceProductionApi.create(acceptanceProductUrl, acceptanceProductionDto);
        Response<AcceptanceProductionDto> response = Response.success(new AcceptanceProductionDto());
        try {
            response = acceptanceDtoCall.execute();
            log.info("Успешно выполнен запрос на создание AcceptanceProductionDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании AcceptanceProductionDto {}", e);
        }
        return response;
    }

    @Override
    public void update(AcceptanceProductionDto acceptanceProductionDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<AcceptanceProductionDto> search(String query) {
        Call<List<AcceptanceProductionDto>> retailShiftDtoListCall = acceptanceProductionApi.search(acceptanceProductUrl, query);
        try {
            acceptanceProductionDtoList = retailShiftDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка AcceptanceProductionDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка AcceptanceProductionDto - ", e);
        }
        return acceptanceProductionDtoList;
    }
}

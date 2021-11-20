package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.services.interfaces.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.api.AcceptanceProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AcceptanceProductionServiceImpl implements AcceptanceProductionService {
    private final AcceptanceProductionApi acceptanceProductionApi;
    private final String acceptanceProductUrl;
    private final CallExecuteService<AcceptanceProductionDto> callExecuteService;
    private AcceptanceProductionDto acceptanceProductionDto;

    public AcceptanceProductionServiceImpl(Retrofit retrofit, @Value("${acceptance_product_url}") String acceptanceProductUrl,
                                           CallExecuteService<AcceptanceProductionDto> callExecuteService) {
        acceptanceProductionApi = retrofit.create(AcceptanceProductionApi.class);
        this.acceptanceProductUrl = acceptanceProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceProductionDto> getAll() {
        return null;
    }

    @Override
    public AcceptanceProductionDto getById(Long id) {
        Call<AcceptanceProductionDto> acceptanceProductionDtoCall = acceptanceProductionApi.getById(acceptanceProductUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceProductionDtoCall, acceptanceProductionDto, AcceptanceProductionDto.class, id);
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
}

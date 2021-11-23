package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.api.AcceptanceProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AcceptanceServiceProductionImpl implements AcceptanceProductionService {

    private final AcceptanceProductionApi acceptanceProductionApi;

    private final String acceptanceProductUrl;

    private final CallExecuteService<AcceptanceProductionDto> callExecuteService;

    public AcceptanceServiceProductionImpl(Retrofit retrofit, @Value("${acceptance_product_url}") String acceptanceProductUrl,
                                           CallExecuteService<AcceptanceProductionDto> callExecuteService) {
        acceptanceProductionApi = retrofit.create(AcceptanceProductionApi.class);
        this.acceptanceProductUrl = acceptanceProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceProductionDto> getAll() {
        Call<List<AcceptanceProductionDto>> acceptanceProductionDto = acceptanceProductionApi.getAll(acceptanceProductUrl);
        return callExecuteService.callExecuteBodyList(acceptanceProductionDto, AcceptanceProductionDto.class);
    }

    @Override
    public AcceptanceProductionDto getById(Long id) {
        Call<AcceptanceProductionDto> acceptanceProductionDtoCall = acceptanceProductionApi.getById(acceptanceProductUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceProductionDtoCall, AcceptanceProductionDto.class, id);
    }
    ;
    @Override
    public List<AcceptanceProductionDto> getByAcceptanceId(Long id) {
        List<AcceptanceProductionDto> acceptanceProductionDtoList = null;
        Call<List<AcceptanceProductionDto>> acceptanceProductionDtoCall = acceptanceProductionApi.getByAcceptanceId(acceptanceProductUrl + "/invoice_product", id);
        try{
            acceptanceProductionDtoList = acceptanceProductionDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceProductDto с Invoice.id = {}", id);
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceProductDto по id= {} - {}", id, e);
        }
        return acceptanceProductionDtoList;
    }

    @Override
    public void create(AcceptanceProductionDto acceptanceProductionDto) {
        Call<Void> acceptanceProductionDtoCall = acceptanceProductionApi.create(acceptanceProductUrl, acceptanceProductionDto);
        callExecuteService.callExecuteBodyCreate(acceptanceProductionDtoCall, AcceptanceProductionDto.class);
    }

    @Override
    public void update(AcceptanceProductionDto acceptanceProductionDto) {
        Call<Void> acceptanceProductionDtoCall = acceptanceProductionApi.create(acceptanceProductUrl, acceptanceProductionDto);
        callExecuteService.callExecuteBodyUpdate(acceptanceProductionDtoCall, AcceptanceProductionDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> acceptanceProductionDtoCall = acceptanceProductionApi.deleteById(acceptanceProductUrl, id);
        callExecuteService.callExecuteBodyDelete(acceptanceProductionDtoCall,AcceptanceProductionDto.class, id);
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.InventarizationDto;
import com.trade_accounting.services.interfaces.InventarizationService;
import com.trade_accounting.services.interfaces.api.InventarizationApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class InventarizationServiceImpl implements InventarizationService {

    private final InventarizationApi inventarizationApi;
    private final String inventarizationUrl;
    private final CallExecuteService<InventarizationDto> callExecuteService;
    private InventarizationDto inventarizationDto;

    public InventarizationServiceImpl(Retrofit retrofit, @Value("${inventarization_url}") String inventarizationUrl, CallExecuteService<InventarizationDto> callExecuteService) {
        inventarizationApi = retrofit.create(InventarizationApi.class);
        this.inventarizationUrl = inventarizationUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<InventarizationDto> getAll() {
        Call<List<InventarizationDto>> invoiceDtoListCall = inventarizationApi.getAll(inventarizationUrl);
        return callExecuteService.callExecuteBodyList(invoiceDtoListCall, InventarizationDto.class);
    }

    @Override
    public InventarizationDto getById(Long id) {
        Call<InventarizationDto> inventarizationDtoGetCall = inventarizationApi.getById(inventarizationUrl, id);
        return callExecuteService.callExecuteBodyById(inventarizationDtoGetCall, InventarizationDto.class, id);
    }

    @Override
    public void create(InventarizationDto dto) {
        Call<Void> inventarizationCreateCall = inventarizationApi.create(inventarizationUrl, dto);
        callExecuteService.callExecuteBodyCreate(inventarizationCreateCall, InventarizationDto.class);
    }

    @Override
    public void update(InventarizationDto dto) {
        Call<Void> inventarizationUpdateCall = inventarizationApi.update(inventarizationUrl, dto);
        callExecuteService.callExecuteBodyUpdate(inventarizationUpdateCall, InventarizationDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> inventarizationDtoCall = inventarizationApi.deleteById(inventarizationUrl, id);
        callExecuteService.callExecuteBodyDelete(inventarizationDtoCall, InventarizationDto.class, id);
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = inventarizationApi.moveToIsRecyclebin(inventarizationUrl, id);
        callExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, InventarizationDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = inventarizationApi.restoreFromIsRecyclebin(inventarizationUrl, id);
        callExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, InventarizationDto.class, id);

    }
}
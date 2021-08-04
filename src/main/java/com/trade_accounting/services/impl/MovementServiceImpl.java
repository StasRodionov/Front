package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.api.MovementApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class MovementServiceImpl implements MovementService {

    private final MovementApi movementApi;
    private final String movementUrl;
    private final CallExecuteService<MovementDto> callExecuteService;

    public MovementServiceImpl(Retrofit retrofit, @Value("${movement_url}") String movementUrl, CallExecuteService<MovementDto> callExecuteService) {
        movementApi = retrofit.create(MovementApi.class);
        this.movementUrl = movementUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<MovementDto> getAll() {
        Call<List<MovementDto>> invoiceDtoListCall = movementApi.getAll(movementUrl);
        return callExecuteService.callExecuteBodyList(invoiceDtoListCall, MovementDto.class);
    }

    @Override
    public MovementDto getById(Long id) {
        return null;
    }

    @Override
    public MovementDto create(MovementDto movementDto) {
        return null;
    }

    @Override
    public void update(MovementDto movementDto) {

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> movementDtoCall = movementApi.deleteById(movementUrl, id);
        callExecuteService.callExecuteBodyDelete(movementDtoCall, MovementDto.class, id);
    }
}

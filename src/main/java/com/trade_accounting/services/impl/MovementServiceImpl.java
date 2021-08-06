package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.api.MovementApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class MovementServiceImpl implements MovementService {

    private final MovementApi movementApi;
    private final String movementUrl;
    private final CallExecuteService<MovementDto> callExecuteService;
    private MovementDto movementDto;

    public MovementServiceImpl(Retrofit retrofit, @Value("${movement_url}") String movementUrl, CallExecuteService<MovementDto> callExecuteService) {
        movementApi = retrofit.create(MovementApi.class);
        this.movementUrl = movementUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<MovementDto> getAll() {
        Call<List<MovementDto>> movementDtoListCall = movementApi.getAll(movementUrl);
        return callExecuteService.callExecuteBodyList(movementDtoListCall, MovementDto.class);
    }

    @Override
    public MovementDto getById(Long id) {
        Call<MovementDto> movementDtoCall = movementApi.getById(movementUrl, id);
        return callExecuteService.callExecuteBodyById(movementDtoCall, movementDto, MovementDto.class, id);
    }

    @Override
    public MovementDto create(MovementDto movementDto) {
        Call<MovementDto> movementDtoCall = movementApi.create(movementUrl, movementDto);

        try {
            movementDto = movementDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение MovementDto - {}", e);
        }

        return movementDto;
    }

    @Override
    public void update(MovementDto movementDto) {
        Call<Void> movementDtoCall = movementApi.update(movementUrl, movementDto);
        try {
            movementDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Movement");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра MovementDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> movementDtoCall = movementApi.deleteById(movementUrl, id);
        callExecuteService.callExecuteBodyDelete(movementDtoCall, MovementDto.class, id);
    }
}

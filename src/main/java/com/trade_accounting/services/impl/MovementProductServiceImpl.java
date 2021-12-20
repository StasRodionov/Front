package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.services.interfaces.MovementProductService;
import com.trade_accounting.services.interfaces.api.MovementProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
@Slf4j
@Service
public class MovementProductServiceImpl implements MovementProductService {

    private final MovementProductApi movementProductApi;
    private final String movementProductUrl;
    private final CallExecuteService<MovementProductDto> callExecuteService;

    public MovementProductServiceImpl(Retrofit retrofit, @Value("${movement_product_url}") String movementProductUrl, CallExecuteService<MovementProductDto> callExecuteService) {
        movementProductApi = retrofit.create(MovementProductApi.class);
        this.movementProductUrl = movementProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<MovementProductDto> getAll() {
        Call<List<MovementProductDto>> movementProductDtoListCall = movementProductApi.getAll(movementProductUrl);
        return callExecuteService.callExecuteBodyList(movementProductDtoListCall, MovementProductDto.class);
    }

    @Override
    public MovementProductDto getById(Long id) {
        Call<MovementProductDto> movementProductDtoCall = movementProductApi.getById(movementProductUrl, id);
        return callExecuteService.callExecuteBodyById(movementProductDtoCall, MovementProductDto.class, id);
    }

    @Override
    public MovementProductDto create(MovementProductDto movementProductDto) {
        Call<MovementProductDto> movementDtoCall = movementProductApi.create(movementProductUrl, movementProductDto);

        try {
            movementProductDto = movementDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание MovementProduct");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение MovementProductDto - {}", e);
        }

        return movementProductDto;
    }

    @Override
    public void update(MovementProductDto movementProductDto) {
        Call<Void> movementProductDtoCall = movementProductApi.update(movementProductUrl, movementProductDto);
        try {
            movementProductDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра MovementProduct");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра MovementProduct - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> movementProductDtoCall = movementProductApi.deleteById(movementProductUrl, id);

        try {
            movementProductDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра MovementProduct с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра MovementProduct с id= {} - {}", e);
        }
    }
}

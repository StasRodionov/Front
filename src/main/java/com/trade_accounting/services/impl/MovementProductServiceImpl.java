package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.services.interfaces.MovementProductService;
import com.trade_accounting.services.interfaces.api.MovementProductApi;
import com.trade_accounting.services.interfaces.api.MovementProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;
@Slf4j
@Service
public class MovementProductServiceImpl implements MovementProductService {

    private final MovementProductApi movementProductApi;
    private final String movementProductUrl;
    private final CallExecuteService<MovementProductDto> callExecuteService;
    private MovementProductDto movementProductDto;

    public MovementProductServiceImpl(Retrofit retrofit, @Value("${movement_product_url}") String movementProductUrl, CallExecuteService<MovementProductDto> callExecuteService) {
        movementProductApi = retrofit.create(MovementProductApi.class);
        this.movementProductUrl = movementProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<MovementProductDto> getAll() {
        return null;
    }

    @Override
    public MovementProductDto getById(Long id) {
        Call<MovementProductDto> movementProductDtoCall = movementProductApi.getById(movementProductUrl, id);
        return callExecuteService.callExecuteBodyById(movementProductDtoCall, movementProductDto, MovementProductDto.class, id);
    }

    @Override
    public MovementProductDto create(MovementProductDto movementProductDto) {
        return null;
    }

    @Override
    public void update(MovementProductDto movementProductDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}

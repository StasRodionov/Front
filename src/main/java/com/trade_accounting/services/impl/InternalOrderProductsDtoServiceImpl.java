package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InternalOrderProductsDto;
import com.trade_accounting.services.interfaces.InternalOrderProductsDtoService;
import com.trade_accounting.services.interfaces.api.InternalOrderProductsDtoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class InternalOrderProductsDtoServiceImpl implements InternalOrderProductsDtoService {
    private final InternalOrderProductsDtoApi internalOrderProductsDtoApi;
    private final String internalOrderProductsDtoUrl;
    private final CallExecuteService<InternalOrderProductsDto> callExecuteService;

    public InternalOrderProductsDtoServiceImpl(Retrofit retrofit,
                                    @Value("${internal_order_product_url}") String internalOrderProductsDtoUrl,
                                    CallExecuteService<InternalOrderProductsDto> callExecuteService) {
        this.internalOrderProductsDtoApi = retrofit.create(InternalOrderProductsDtoApi.class);
        this.internalOrderProductsDtoUrl = internalOrderProductsDtoUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public InternalOrderProductsDto create(InternalOrderProductsDto internalOrderProductsDto) {
        Call<InternalOrderProductsDto> internalProductsDtoCall = internalOrderProductsDtoApi.create(internalOrderProductsDtoUrl,
                internalOrderProductsDto);

        try {
            internalOrderProductsDto = internalProductsDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение InternalOrderDto - {}", e);
        }

        return internalOrderProductsDto;
    }

    @Override
    public InternalOrderProductsDto getById(Long id) {
        Call<InternalOrderProductsDto> internalDtoListCall = internalOrderProductsDtoApi.getById(internalOrderProductsDtoUrl, id);
        return callExecuteService.callExecuteBodyById(internalDtoListCall, InternalOrderProductsDto.class, id);
    }

    @Override
    public List<InternalOrderProductsDto> getAll() {
        Call<List<InternalOrderProductsDto>> internalDtoListCall = internalOrderProductsDtoApi.getAll(internalOrderProductsDtoUrl);
        return callExecuteService.callExecuteBodyList(internalDtoListCall, InternalOrderProductsDto.class);
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.services.interfaces.InternalOrderService;
import com.trade_accounting.services.interfaces.api.InternalOrderApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InternalOrderServiceImpl implements InternalOrderService {
    private final InternalOrderApi internalOrderApi;
    private final String internalOrderUrl;
    private final CallExecuteService<InternalOrderDto> callExecuteService;

    public InternalOrderServiceImpl(Retrofit retrofit,
                                    @Value("${internal_order_url}") String internalOrderUrl,
                                    CallExecuteService<InternalOrderDto> callExecuteService) {
        this.internalOrderApi = retrofit.create(InternalOrderApi.class);
        this.internalOrderUrl = internalOrderUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<InternalOrderDto> getAll() {
        Call<List<InternalOrderDto>> internalDtoListCall = internalOrderApi.getAll(internalOrderUrl);
        return callExecuteService.callExecuteBodyList(internalDtoListCall, InternalOrderDto.class);
    }

    @Override
    public InternalOrderDto getById(Long id) {
        Call<InternalOrderDto> internalDtoListCall = internalOrderApi.getById(internalOrderUrl, id);
        return callExecuteService.callExecuteBodyById(internalDtoListCall, InternalOrderDto.class, id);
    }

    @Override
    public InternalOrderDto create(InternalOrderDto internalOrderDto) {
        Call<InternalOrderDto> internalDtoCall = internalOrderApi.create(internalOrderUrl, internalOrderDto);

        try {
            internalOrderDto = internalDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение InternalOrderDto - {}", e);
        }

        return internalOrderDto;
    }

    @Override
    public void update(InternalOrderDto internalOrderDto) {
        Call<Void> internalOrderDtoCall = internalOrderApi.update(internalOrderUrl, internalOrderDto);
        try {
            internalOrderDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра InternalOrder");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра InternalOrderDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> internalOrderDtoCall = internalOrderApi.deleteById(internalOrderUrl, id);

        try {
            internalOrderDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра InternalOrderDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра InternalOrderDto с id= {} - {}", e);
        }
    }

    @Override
    public List<InternalOrderDto> searchByTerm(String searchItem) {
        List<InternalOrderDto> internalOrderDtoList = new ArrayList<>();
        Call<List<InternalOrderDto>> internalOrderDtoListCall = internalOrderApi.getAll(internalOrderUrl, searchItem);
        try {
            internalOrderDtoList = internalOrderDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InternalOrderDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка InternalOrderDto: {IOException}", e);
        }
        return internalOrderDtoList;
    }

    @Override
    public List<InternalOrderDto> searchByFilter(Map<String, String> query) {
        List<InternalOrderDto> internalOrderDtoList = new ArrayList<>();
        Call<List<InternalOrderDto>> internalOrderDtoListCall = internalOrderApi.searchByFilter(internalOrderUrl, query);
        try {
            internalOrderDtoList = internalOrderDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка InternalOrderDto по ФИЛЬТРУ -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка InternalOrderDto - ", e);
        }
        return internalOrderDtoList;
    }
}

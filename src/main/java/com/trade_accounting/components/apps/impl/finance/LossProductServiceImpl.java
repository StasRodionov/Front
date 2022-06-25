package com.trade_accounting.components.apps.impl.finance;

import com.trade_accounting.models.dto.finance.LossProductDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.finance.LossProductService;
import com.trade_accounting.services.api.finance.LossProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class LossProductServiceImpl implements LossProductService {

    private final LossProductApi lossProductApi;
    private final String lossProductUrl;
    private final CallExecuteService<LossProductDto> callExecuteService;
    private LossProductDto lossProductDto;

    public LossProductServiceImpl(Retrofit retrofit, @Value("${loss_product_url}") String lossProductUrl, CallExecuteService<LossProductDto> callExecuteService) {
        this.lossProductApi = retrofit.create(LossProductApi.class);
        this.lossProductUrl = lossProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<LossProductDto> getAll() {
        Call<List<LossProductDto>> lossProductDtoListCall = lossProductApi.getAll(lossProductUrl);
        return callExecuteService.callExecuteBodyList(lossProductDtoListCall, LossProductDto.class);
    }

    @Override
    public LossProductDto getById(Long id) {
        Call<LossProductDto> lossProductDtoCall = lossProductApi.getById(lossProductUrl, id);
        return callExecuteService.callExecuteBodyById(lossProductDtoCall, LossProductDto.class, id);
    }

    @Override
    public LossProductDto create(LossProductDto lossProductDto) {
        Call<LossProductDto> lossProductDtoCall = lossProductApi.create(lossProductUrl, lossProductDto);

        try {
            lossProductDto = lossProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание LossProduct");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение LossProductDto - {}", e);
        }

        return lossProductDto;
    }

    @Override
    public void update(LossProductDto lossProductDto) {
        Call<Void> lossProductDtoCall = lossProductApi.update(lossProductUrl, lossProductDto);
        try {
            lossProductDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра LossProduct");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра LossProductDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> lossProductDtoCall = lossProductApi.deleteById(lossProductUrl, id);

        try {
            lossProductDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра LossProductDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра LossProductDto с id= {} - {}", e);
        }
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.trade_accounting.services.interfaces.api.RetailStoreApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RetailStoreServiceImpl implements RetailStoreService {

    private final RetailStoreApi retailStoreApi;
    private final String retailStoreUrl;

    public RetailStoreServiceImpl(Retrofit retrofit, @Value("${retail_stores_url}")String retailStoreUrl) {
        retailStoreApi = retrofit.create(RetailStoreApi.class);
        this.retailStoreUrl = retailStoreUrl;
    }

    @Override
    public List<RetailStoreDto> getAll() {

        List<RetailStoreDto> retailStoreDtoList = new ArrayList<>();
        Call<List<RetailStoreDto>> retailStoreDtoListCall = retailStoreApi.getAll(retailStoreUrl);

        try {
            retailStoreDtoList.addAll(retailStoreDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка RetailStoreDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка RetailStoreDto - {}", e);
        }
        return retailStoreDtoList;
    }

    @Override
    public RetailStoreDto getById(Long id) {

        RetailStoreDto retailStoreDto = null;
        Call<RetailStoreDto> retailStoreDtoCall = retailStoreApi.getById(retailStoreUrl, id);

        try {
            retailStoreDto = retailStoreDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экзаепляра RetailStoreDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра RetailStoreDto - {}", e);
        }
        return retailStoreDto;
    }

    @Override
    public void create(RetailStoreDto retailStoreDto) {

        Call<Void> retailStoreDtoCall = retailStoreApi.create(retailStoreUrl, retailStoreDto);

        try {
            retailStoreDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра RetailStoreDto");
        }
        catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра RetailStoreDto - {}", e);
        }

    }

    @Override
    public void update(RetailStoreDto retailStoreDto) {
        Call<Void> retailStoreDtoCall = retailStoreApi.update(retailStoreUrl, retailStoreDto);

        try {
            retailStoreDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра RetailStoreDto");
        }
        catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RetailStoreDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailStoreDtoCall = retailStoreApi.deleteById(retailStoreUrl, id);

        try {
            retailStoreDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра RetailStoreDto");
        }
        catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра RetailStoreDto - {}", e);
        }
    }
}

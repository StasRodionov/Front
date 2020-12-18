package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.trade_accounting.services.interfaces.api.WarehouseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseApi warehouseApi;
    private final String warehouseUrl;
    private List<WarehouseDto> warehouseDtoList;
    private WarehouseDto warehouseDto;

    @Autowired
    public WarehouseServiceImpl(@Value("${warehouse_url}") String warehouseUrl, Retrofit retrofit) {

        this.warehouseUrl = warehouseUrl;
        warehouseApi = retrofit.create(WarehouseApi.class);
    }

    @Override
    public List<WarehouseDto> getAll() {

        Call<List<WarehouseDto>> warehouseDtoListCall = warehouseApi.getAll(warehouseUrl);

        warehouseDtoListCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<WarehouseDto>> call, Response<List<WarehouseDto>> response) {
                if (response.isSuccessful()) {
                    warehouseDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка WarehouseDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка WarehouseDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<WarehouseDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка WarehouseDto", throwable);
            }
        });

        return warehouseDtoList;
    }

    @Override
    public WarehouseDto getById(Long id) {

        Call<WarehouseDto> warehouseDtoCall = warehouseApi.getById(warehouseUrl, id);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<WarehouseDto> call, Response<WarehouseDto> response) {
                if (response.isSuccessful()) {
                    warehouseDto = response.body();
                    log.info("Успешно выполнен запрос на получение экзаепляра WarehouseDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра WarehouseDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<WarehouseDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра WarehouseDto", throwable);
            }
        });

        return warehouseDto;
    }

    @Override
    public void create(WarehouseDto warehouseDto) {

        Call<Void> warehouseDtoCall = warehouseApi.create(warehouseUrl, warehouseDto);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра WarehouseDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра WarehouseDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра WarehouseDto", throwable);
            }
        });
    }

    @Override
    public void update(WarehouseDto warehouseDto) {

        Call<Void> warehouseDtoCall = warehouseApi.update(warehouseUrl, warehouseDto);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра WarehouseDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра WarehouseDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра WarehouseDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> warehouseDtoCall = warehouseApi.deleteById(warehouseUrl, id);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра WarehouseDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра WarehouseDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра WarehouseDto", throwable);
            }
        });
    }
}

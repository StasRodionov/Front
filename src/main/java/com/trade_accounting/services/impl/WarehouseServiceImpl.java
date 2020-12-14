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

@Service
@Slf4j
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<WarehouseDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка WarehouseDto");
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<WarehouseDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении WarehouseDto с id = {}", id);
            }
        });

        return warehouseDto;
    }

    @Override
    public void create(WarehouseDto warehouseDto) {

        Call<WarehouseDto> warehouseDtoCall = warehouseApi.create(warehouseUrl, warehouseDto);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<WarehouseDto> call, Response<WarehouseDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<WarehouseDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", warehouseDto);
            }
        });
    }

    @Override
    public void update(WarehouseDto warehouseDto) {

        Call<WarehouseDto> warehouseDtoCall = warehouseApi.update(warehouseUrl, warehouseDto);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<WarehouseDto> call, Response<WarehouseDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<WarehouseDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", warehouseDto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {

        Call<WarehouseDto> warehouseDtoCall = warehouseApi.deleteById(warehouseUrl, id);

        warehouseDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<WarehouseDto> call, Response<WarehouseDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<WarehouseDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра CompanyDto c id = {}", id);
            }
        });
    }
}

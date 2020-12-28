package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.api.UnitApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UnitServiceImpl implements UnitService {

    private final UnitApi unitApi;

    private final String unitUrl;


    private UnitDto unitDto;

    public UnitServiceImpl(@Value("${unit_url}") String unitUrl, Retrofit retrofit) {
        this.unitUrl = unitUrl;
        unitApi = retrofit.create(UnitApi.class);
    }

    @Override
    public List<UnitDto> getAll() {

        final List<UnitDto> unitDtoList = new ArrayList<>();

        Call<List<UnitDto>> unitDtoListCall = unitApi.getAll(unitUrl);

        try {
            unitDtoList.addAll(unitDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка UnitDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка UnitDto");
        }
/*        unitDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<UnitDto>> call, Response<List<UnitDto>> response) {
                if (response.isSuccessful()) {
                    unitDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка UnitDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка UnitDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<UnitDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка UnitDto", throwable);
            }
        });
*/
        return unitDtoList;
    }

    @Override
    public UnitDto getById(Long id) {
        Call<UnitDto> unitDtoCall = unitApi.getById(unitUrl, id);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UnitDto> call, Response<UnitDto> response) {
                if (response.isSuccessful()) {
                    unitDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра UnitDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра UnitDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnitDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра UnitDto по id", throwable);
            }
        });

        return unitDto;
    }

    @Override
    public void create(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.create(unitUrl, unitDto);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра UnitDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра UnitDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра UnitDto", throwable);
            }
        });
    }

    @Override
    public void update(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.update(unitUrl, unitDto);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра UnitDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра UnitDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра UnitDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> unitDtoCall = unitApi.deleteById(unitUrl, id);

        unitDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра UnitDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра UnitDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра UnitDto", throwable);
            }
        });
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PositionDto;
import com.trade_accounting.services.interfaces.PositionService;
import lombok.extern.slf4j.Slf4j;
import com.trade_accounting.services.interfaces.api.PositionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionApi positionApi;

    private final String positionUrl;

    private List<PositionDto> positionDtoList;

    private PositionDto positionDto;

    @Autowired
    public PositionServiceImpl(@Value("${position_url}") String positionUrl, Retrofit retrofit) {

        this.positionUrl = positionUrl;

        positionApi = retrofit.create(PositionApi.class);

    }

    @Override
    public List<PositionDto> getAll() {

        Call<List<PositionDto>> positionDtoListCall = positionApi.getAll(positionUrl);

        positionDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<PositionDto>> call, Response<List<PositionDto>> response) {
                if (response.isSuccessful()) {
                    positionDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка PositionDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка PositionDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<PositionDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка PositionDto", throwable);
            }
        });

        return positionDtoList;
    }

    @Override
    public PositionDto getById(Long id) {
        Call<PositionDto> positionDtoCall = positionApi.getById(positionUrl, id);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PositionDto> call, Response<PositionDto> response) {
                if (response.isSuccessful()) {
                    positionDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра PositionDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра PositionDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PositionDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра PositionDto по id", throwable);
            }
        });

        return positionDto;
    }

    @Override
    public void create(PositionDto positionDto) {

        Call<Void> positionDtoCall = positionApi.create(positionUrl, positionDto);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра PositionDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра PositionDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра PositionDto", throwable);
            }
        });

    }

    @Override
    public void update(PositionDto positionDto) {

        Call<Void> positionDtoCall = positionApi.update(positionUrl, positionDto);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра PositionDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PositionDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра PositionDto", throwable);
            }
        });

    }

    @Override
    public void deleteById(Long id) {

        Call<Void> positionDtoCall = positionApi.deleteById(positionUrl, id);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра PositionDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PositionDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра PositionDto", throwable);
            }
        });

    }

    /*@PostConstruct
    public void test() {
        getAll();
        getById(1L);
        create(new PositionDto(null, "fff", "ddd"));
        update(new PositionDto(1L, "Генеральный директор", "1"));
        deleteById(21L);

    }*/
}

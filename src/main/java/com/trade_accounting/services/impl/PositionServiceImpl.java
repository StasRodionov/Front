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

@Service
@Slf4j
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<PositionDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка PositionDto");
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PositionDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении PositionDto c id = {}", id);
            }
        });

        return positionDto;
    }

    @Override
    public void create(PositionDto positionDto) {

        Call<PositionDto> positionDtoCall = positionApi.create(positionUrl, positionDto);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PositionDto> call, Response<PositionDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PositionDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", positionDto);
            }
        });

    }

    @Override
    public void update(PositionDto positionDto) {

        Call<PositionDto> positionDtoCall = positionApi.update(positionUrl, positionDto);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PositionDto> call, Response<PositionDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PositionDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", positionDto);
            }
        });

    }

    @Override
    public void deleteById(Long id) {

        Call<PositionDto> positionDtoCall = positionApi.deleteById(positionUrl, id);

        positionDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PositionDto> call, Response<PositionDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PositionDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра PositionDto c id = {}", id);
            }
        });

    }

    /*@PostConstruct
    public void test() {
        getAll();
        getById(1L);
        create(new PositionDto(null, "fff", "ddd"));
        update(new PositionDto(1L, "Генеральный директор", "1"));
        deleteById(22L);

    }*/
}

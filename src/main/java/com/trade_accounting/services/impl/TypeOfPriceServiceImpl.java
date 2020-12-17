package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.trade_accounting.services.interfaces.api.TypeOfPriceApi;
import lombok.extern.slf4j.Slf4j;
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
public class TypeOfPriceServiceImpl implements TypeOfPriceService {

    private final TypeOfPriceApi typeOfPriceApi;

    private final String typeOfPriceUrl;

    private List<TypeOfPriceDto> typeOfPriceDtoList;

    private TypeOfPriceDto typeOfPriceDto;

    @Autowired
    public TypeOfPriceServiceImpl(@Value("${typeofprice_url}") String typeOfPriceUrl, Retrofit retrofit) {

        this.typeOfPriceUrl = typeOfPriceUrl;

        typeOfPriceApi = retrofit.create(TypeOfPriceApi.class);

    }

//    @PostConstruct
//    public void init(){
//        getAll();
//        getById(1L);
//        create(new TypeOfPriceDto(null,"fff","ddd"));
//        update(new TypeOfPriceDto(2L,"fffddd","ddd"));
//        deleteById(2L);
//    }


    @Override
    public List<TypeOfPriceDto> getAll() {
        Call<List<TypeOfPriceDto>> typeOfPriceDtoListCall = typeOfPriceApi.getAll(typeOfPriceUrl);

       typeOfPriceDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TypeOfPriceDto>> call, Response<List<TypeOfPriceDto>> response) {
                if (response.isSuccessful()) {
                    typeOfPriceDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка TypeOfPriceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка TypeOfPriceDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TypeOfPriceDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка TypeOfPriceDto", throwable);
            }
        });

        return typeOfPriceDtoList;
    }

    @Override
    public TypeOfPriceDto getById(Long id) {
        Call<TypeOfPriceDto> typeOfPriceDtoCall = typeOfPriceApi.getById(typeOfPriceUrl, id);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TypeOfPriceDto> call, Response<TypeOfPriceDto> response) {
                if (response.isSuccessful()) {
                    typeOfPriceDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра TypeOfPriceDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра TypeOfPriceDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TypeOfPriceDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра TypeOfPriceDto по id", throwable);
            }
        });

        return typeOfPriceDto;
    }

    @Override
    public void create(TypeOfPriceDto typeOfPriceDto) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.create(typeOfPriceUrl, typeOfPriceDto);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра TypeOfPriceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра TypeOfPriceDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра TypeOfPriceDto", throwable);
            }
        });
    }

    @Override
    public void update(TypeOfPriceDto typeOfPriceDto) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.update(typeOfPriceUrl, typeOfPriceDto);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра TypeOfPriceDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра TypeOfPriceDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра TypeOfPriceDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> typeOfPriceDtoCall = typeOfPriceApi.deleteById(typeOfPriceUrl, id);

        typeOfPriceDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра TypeOfPriceDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра TypeOfPriceDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра TypeOfPriceDto", throwable);
            }
        });
    }
}

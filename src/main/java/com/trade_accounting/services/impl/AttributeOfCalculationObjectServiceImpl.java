package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AttributeOfCalculationObjectDto;
import com.trade_accounting.services.interfaces.AttributeOfCalculationObjectService;
import com.trade_accounting.services.interfaces.api.AttributeOfCalculationObjectApi;
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

@Slf4j
@Service
public class AttributeOfCalculationObjectServiceImpl implements AttributeOfCalculationObjectService {

    private final AttributeOfCalculationObjectApi attributeOfCalculationObjectApi;

    private final String attributeOfCalculationObjectUrl;

    private List<AttributeOfCalculationObjectDto> attributeOfCalculationObjectDtoList;

    private AttributeOfCalculationObjectDto attributeOfCalculationObjectDto;

    @Autowired
    public AttributeOfCalculationObjectServiceImpl(@Value("${attribute_calculation_object_url}") String attributeOfCalculationObjectUrl, Retrofit retrofit) {

        this.attributeOfCalculationObjectUrl = attributeOfCalculationObjectUrl;
        attributeOfCalculationObjectApi = retrofit.create(AttributeOfCalculationObjectApi.class);
    }

    @Override
    public List<AttributeOfCalculationObjectDto> getAll() {
        Call<List<AttributeOfCalculationObjectDto>> attributeOfCalculationObjectDtoListCall = attributeOfCalculationObjectApi.getAll(attributeOfCalculationObjectUrl);

        attributeOfCalculationObjectDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<AttributeOfCalculationObjectDto>> call, Response<List<AttributeOfCalculationObjectDto>> response) {
                if (response.isSuccessful()) {
                    attributeOfCalculationObjectDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка AttributeOfCalculationObjectDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка AttributeOfCalculationObjectDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<AttributeOfCalculationObjectDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка AttributeOfCalculationObject)Dto", throwable);
            }
        });

        return attributeOfCalculationObjectDtoList;
    }

    @Override
    public AttributeOfCalculationObjectDto getById(Long id) {
        Call<AttributeOfCalculationObjectDto> AttributeOfCalculationDtoCall = attributeOfCalculationObjectApi.getById(attributeOfCalculationObjectUrl, id);

        AttributeOfCalculationDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AttributeOfCalculationObjectDto> call, Response<AttributeOfCalculationObjectDto> response) {
                if (response.isSuccessful()) {
                    attributeOfCalculationObjectDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра AttributeOfCalculationObjectDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра AttributeOfCalculationObjectDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AttributeOfCalculationObjectDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра AttributeOfCalculationObjectDto по id", throwable);
            }
        });
        return attributeOfCalculationObjectDto;
    }

    @Override
    public void create(AttributeOfCalculationObjectDto attribute) {

        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.create(attributeOfCalculationObjectUrl, attribute);

        attributeOfCalculationObjectDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра AttributeOfCalculationObjectDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра AttributeOfCalculationObjectDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра AttributeOfCalculationObjectDto", throwable);
            }
        });

    }

    @Override
    public void update(AttributeOfCalculationObjectDto attribute) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.update(attributeOfCalculationObjectUrl, attribute);

        attributeOfCalculationObjectDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра AttributeOfCalculationObjectDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра AttributeOfCalculationObjectDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра AttributeOfCalculationObjectDto", throwable);
            }
        });

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.deleteById(attributeOfCalculationObjectUrl, id);

        attributeOfCalculationObjectDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра AttributeOfCalculationObjectDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра AttributeOfCalculationObjectDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра AttributeOfCalculationObjectDto", throwable);
            }
        });
    }

//    @PostConstruct
//    public void test() {
//        create(new AttributeOfCalculationObjectDto(null, "special", "7", true));
//        getAll();
//        getById(1L);
//        update(new AttributeOfCalculationObjectDto(1L, "not special", "8", false));
//        deleteById(1L);
//    }
}

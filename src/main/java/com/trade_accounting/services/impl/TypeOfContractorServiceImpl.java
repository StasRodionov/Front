package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.api.TypeOfContractorApi;
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
public class TypeOfContractorServiceImpl implements TypeOfContractorService {

    private final TypeOfContractorApi typeOfContractorApi;
    private final String typeOfContractorUrl;
    private List<TypeOfContractorDto> typeOfContractorDtoList;
    private TypeOfContractorDto typeOfContractorDto;

    @Autowired
    public TypeOfContractorServiceImpl(@Value("${typeOfContractor_url}") String typeOfContractorUrl, Retrofit retrofit) {

        this.typeOfContractorUrl = typeOfContractorUrl;
        typeOfContractorApi = retrofit.create(TypeOfContractorApi.class);
    }

    @Override
    public List<TypeOfContractorDto> getAll() {

        Call<List<TypeOfContractorDto>> typeOfContractorDtoListCall = typeOfContractorApi.getAll(typeOfContractorUrl);

        typeOfContractorDtoListCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<TypeOfContractorDto>> call, Response<List<TypeOfContractorDto>> response) {
                if (response.isSuccessful()) {
                    typeOfContractorDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка TypeOfContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка TypeOfContractorDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<TypeOfContractorDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка TypeOfContractorDto", throwable);
            }
        });

        return typeOfContractorDtoList;
    }

    @Override
    public TypeOfContractorDto getById(Long id) {

        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.getById(typeOfContractorUrl, id);

        typeOfContractorDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<TypeOfContractorDto> call, Response<TypeOfContractorDto> response) {
                if (response.isSuccessful()) {
                    typeOfContractorDto = response.body();
                    log.info("Успешно выполнен запрос на получение экзаепляра TypeOfContractorDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра TypeOfContractorDto c id = {} - {}", id, response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<TypeOfContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра TypeOfContractorDto", throwable);
            }
        });

        return typeOfContractorDto;
    }

    @Override
    public void create(TypeOfContractorDto typeOfContractorDto) {

        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.create(typeOfContractorUrl, typeOfContractorDto);

        typeOfContractorDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<TypeOfContractorDto> call, Response<TypeOfContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра TypeOfContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра TypeOfContractorDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<TypeOfContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра TypeOfContractorDto", throwable);
            }
        });
    }

    @Override
    public void update(TypeOfContractorDto typeOfContractorDto) {

        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.update(typeOfContractorUrl, typeOfContractorDto);

        typeOfContractorDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<TypeOfContractorDto> call, Response<TypeOfContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра TypeOfContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра TypeOfContractorDto - {}", response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<TypeOfContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра TypeOfContractorDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {

        Call<TypeOfContractorDto> typeOfContractorDtoCall = typeOfContractorApi.deleteById(typeOfContractorUrl, id);

        typeOfContractorDtoCall.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<TypeOfContractorDto> call, Response<TypeOfContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра TypeOfContractorDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра TypeOfContractorDto с id = {} - {}", id, response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<TypeOfContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра TypeOfContractorDto", throwable);
            }
        });
    }
}

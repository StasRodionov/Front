package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaxSystemDto;
import com.trade_accounting.services.interfaces.TaxSystemService;
import com.trade_accounting.services.interfaces.api.TaxSystemApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TaxSystemServiceImpl implements TaxSystemService {

    private final TaxSystemApi taxSystemApi;
    private final String taxSystemUrl;
    private List<TaxSystemDto> taxSystemDtoList;
    private TaxSystemDto taxSystemDto;

    public TaxSystemServiceImpl(@Value("${tax_system_url}") String taxSystemUrl, Retrofit retrofit) {
        taxSystemApi = retrofit.create(TaxSystemApi.class);
        this.taxSystemUrl = taxSystemUrl;
    }

    @Override
    public List<TaxSystemDto> getAll() {
        Call<List<TaxSystemDto>> taxSystemDtoListCall = taxSystemApi.getAll(taxSystemUrl);

        taxSystemDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TaxSystemDto>> call, Response<List<TaxSystemDto>> response) {
                if (response.isSuccessful()) {
                    taxSystemDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка TaxSystemDto");
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение списка TaxSystemDto: {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TaxSystemDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение списка TaxSystemDto: ", throwable);
            }
        });

        return taxSystemDtoList;
    }

    @Override
    public TaxSystemDto getById(Long id) {
        Call<TaxSystemDto> taxSystemDtoCall = taxSystemApi.getById(taxSystemUrl, id);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TaxSystemDto> call, Response<TaxSystemDto> response) {
                if (response.isSuccessful()) {
                    taxSystemDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра TaxSystemDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение TaxSystemDto с id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TaxSystemDto> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение taxSystemDto c id = {}: {}",
                        id, throwable);
            }
        });
        return taxSystemDto;
    }

    @Override
    public void create(TaxSystemDto taxSystemDto) {
        Call<Void> taxSystemDtoCall = taxSystemApi.create(taxSystemUrl, taxSystemDto);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание нового экземпляра {}", taxSystemDto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                            taxSystemDto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                        taxSystemDto, throwable);
            }
        });
    }

    @Override
    public void update(TaxSystemDto taxSystemDto) {
        Call<Void> taxSystemDtoCall = taxSystemApi.update(taxSystemUrl, taxSystemDto);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление {}", taxSystemDto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                            taxSystemDto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                        taxSystemDto, throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taxSystemDtoCall = taxSystemApi.deleteById(taxSystemUrl, id);

        taxSystemDtoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление TaxSystemDto c id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на удаление TaxSystemDto c id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на удаление TaxSystemDto c id = {}: {}",
                        id, throwable);
            }
        });
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaxSystemDto;
import com.trade_accounting.services.interfaces.TaxSystemService;
import com.trade_accounting.services.interfaces.api.TaxSystemApi;
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
public class TaxSystemServiceImpl implements TaxSystemService {

    private final TaxSystemApi taxSystemApi;

    private final String taxSystemUrl;

    private List<TaxSystemDto> taxSystemDtoList;

    private TaxSystemDto taxSystemDto;

    @Autowired
    public TaxSystemServiceImpl(@Value("${taxsystem_url}") String taxSystemUrl, Retrofit retrofit) {
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<TaxSystemDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка TaxSystemDto");
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
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TaxSystemDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении taxSystemDto c id = {}", id);
            }
        });
        return taxSystemDto;
    }

    @Override
    public void create(TaxSystemDto taxSystemDto) {
        Call<TaxSystemDto> taxSystemDtoCall = taxSystemApi.create(taxSystemUrl, taxSystemDto);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TaxSystemDto> call, Response<TaxSystemDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TaxSystemDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", taxSystemDto);
            }
        });
    }

    @Override
    public void update(TaxSystemDto taxSystemDto) {
        Call<TaxSystemDto> taxSystemDtoCall = taxSystemApi.update(taxSystemUrl, taxSystemDto);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TaxSystemDto> call, Response<TaxSystemDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TaxSystemDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", taxSystemDto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<TaxSystemDto> taxSystemDtoCall = taxSystemApi.deleteById(taxSystemUrl, id);

        taxSystemDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TaxSystemDto> call, Response<TaxSystemDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TaxSystemDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление taxSystemDto c id = {}", id);
            }
        });
    }
}

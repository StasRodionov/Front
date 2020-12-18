package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.api.CompanyApi;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyApi companyApi;

    private final String companyUrl;

    private List<CompanyDto> companyDtoList;

    private CompanyDto companyDto;

    public CompanyServiceImpl(@Value("${company_url}") String companyUrl, Retrofit retrofit) {
        this.companyUrl = companyUrl;
        companyApi = retrofit.create(CompanyApi.class);
    }

    @Override
    public List<CompanyDto> getAll() {
        Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll(companyUrl);

        companyDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CompanyDto>> call, Response<List<CompanyDto>> response) {
                if (response.isSuccessful()) {
                    companyDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<CompanyDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка CompanyDto");
            }
        });

        return companyDtoList;
    }

    @Override
    public CompanyDto getById(Long id) {
        Call<CompanyDto> companyDtoCall = companyApi.getById(companyUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (response.isSuccessful()) {
                    companyDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении CompanyDto c id = {}", id);
            }
        });

        return companyDto;
    }

    @Override
    public CompanyDto getByEmail(String email) {
        Call<CompanyDto> companyDtoCall = companyApi.getByEmail(companyUrl, email);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (response.isSuccessful()) {
                    companyDto = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении CompanyDto c email = {}", email);
            }
        });

        return companyDto;
    }

    @Override
    public void create(CompanyDto companyDto) {
        Call<CompanyDto> companyDtoCall = companyApi.create(companyUrl, companyDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", companyDto);
            }
        });
    }

    @Override
    public void update(CompanyDto companyDto) {
        Call<CompanyDto> companyDtoCall = companyApi.update(companyUrl, companyDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", companyDto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<CompanyDto> companyDtoCall = companyApi.deleteById(companyUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра CompanyDto c id = {}", id);
            }
        });
    }
}

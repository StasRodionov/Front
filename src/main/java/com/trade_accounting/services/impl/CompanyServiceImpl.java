package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyApi;
import com.trade_accounting.services.interfaces.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyApi companyApi;

    private List<CompanyDto> companyDtoList;

    private CompanyDto companyDto;

    @Autowired
    public CompanyServiceImpl(@Value("${base_url}") String baseUrl, @Value("${company_url}") String companyUrl) {

        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(baseUrl + companyUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        companyApi = retrofitClient.create(CompanyApi.class);
    }

    @Override
    public List<CompanyDto> getAll() {
        Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll();

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
                //TODO Logging
            }
        });

        return companyDtoList;
    }

    @Override
    public CompanyDto getById(String id) {
        Call<CompanyDto> companyDtoCall = companyApi.getById(id);

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
                //TODO Logging
            }
        });

        return companyDto;
    }

    @Override
    public void add(CompanyDto companyDto) {
        Call<CompanyDto> companyDtoCall = companyApi.add(companyDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void update(CompanyDto companyDto) {
        Call<CompanyDto> companyDtoCall = companyApi.update(companyDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }

    @Override
    public void deleteById(String id) {
        Call<CompanyDto> companyDtoCall = companyApi.deleteById(id);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                //TODO Logging
            }
        });
    }
}

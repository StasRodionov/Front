package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.api.ContractorGroupApi;
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
public class ContractorGroupServiceImpl implements ContractorGroupService {

    private final ContractorGroupApi contractorGroupApi;

    private final String contractorGroupUrl;

    private ContractorGroupDto contractorGroupDto;

    private List<ContractorGroupDto> contractorGroupDtoList;

    public ContractorGroupServiceImpl(@Value("${contractorGroup_url}") String contractorGroupUrl, Retrofit retrofit) {
        this.contractorGroupApi = retrofit.create(ContractorGroupApi.class);
        this.contractorGroupUrl = contractorGroupUrl;
    }

    @Override
    public List<ContractorGroupDto> getAll() {
        Call<List<ContractorGroupDto>> contractorGroupDtoCallList = contractorGroupApi.getAll(contractorGroupUrl);

        contractorGroupDtoCallList.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ContractorGroupDto>> call, Response<List<ContractorGroupDto>> response) {
                if (response.isSuccessful()) {
                    contractorGroupDtoList = response.body();
                } else {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ContractorGroupDto>> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении списка ContractorGroupDto");
            }
        });
        return contractorGroupDtoList;
    }

    @Override
    public ContractorGroupDto getById(Long id) {
        Call<ContractorGroupDto> contractorGroupDtoCall = contractorGroupApi.getById(contractorGroupUrl, id);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorGroupDto> call, Response<ContractorGroupDto> response) {
             if (response.isSuccessful()) {
                 contractorGroupDto = response.body();
             } else {
                 System.out.println("Response error " + response.body());
             }
            }

            @Override
            public void onFailure(Call<ContractorGroupDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при получении ContractorGroupDto c id = {}", id);
            }
        });
        return contractorGroupDto;
    }

    @Override
    public void create(ContractorGroupDto dto) {
        Call<ContractorGroupDto> contractorGroupDtoCall = contractorGroupApi.create(contractorGroupUrl, dto);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorGroupDto> call, Response<ContractorGroupDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorGroupDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на создание нового экземпляра {}", dto);
            }
        });
    }

    @Override
    public void update(ContractorGroupDto dto) {
        Call<ContractorGroupDto> contractorGroupDtoCall = contractorGroupApi.update(contractorGroupUrl, dto);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorGroupDto> call, Response<ContractorGroupDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorGroupDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на обновление экземпляра {}", dto);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<ContractorGroupDto> contractorGroupDtoCall = contractorGroupApi.deleteById(contractorGroupUrl, id);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorGroupDto> call, Response<ContractorGroupDto> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Response error " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorGroupDto> call, Throwable throwable) {
                log.debug("Произошла ошибка при отправке запроса на удаление экземпляра ContractorGroupDto c id = {}", id);
            }
        });
    }
}

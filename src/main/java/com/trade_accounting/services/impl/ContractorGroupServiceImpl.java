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

    public ContractorGroupServiceImpl(@Value("${contractor_group_url}") String contractorGroupUrl, Retrofit retrofit) {
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
                    log.info("Успешно выполнен запрос на получение списка ContractorGroupDto");
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение списка ContractorGroupDto: {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ContractorGroupDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение списка ContractorGroupDto: ", throwable);
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
                    log.info("Успешно выполнен запрос на получение экземпляра ContractorGroupDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение ContractorGroupDto с id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorGroupDto> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение ContractorGroupDto c id = {}: {}",
                        id, throwable);
            }
        });
        return contractorGroupDto;
    }

    @Override
    public void create(ContractorGroupDto dto) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.create(contractorGroupUrl, dto);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание нового экземпляра {}", dto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                            dto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                        dto, throwable);
            }
        });
    }

    @Override
    public void update(ContractorGroupDto dto) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.update(contractorGroupUrl, dto);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление {}", dto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                            dto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                        dto, throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.deleteById(contractorGroupUrl, id);

        contractorGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление ContractorGroupDto c id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на удаление ContractorGroupDto c id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на удаление ContractorGroupDto c id = {}: {}",
                        id, throwable);
            }
        });
    }
}
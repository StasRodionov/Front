package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.api.ContractorApi;
import lombok.extern.slf4j.Slf4j;
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
public class ContractorServiceImpl implements ContractorService {

    private final String contractorUrl;

    private final ContractorApi contractorApi;

    private List<ContractorDto> contractorDtoList;

    private ContractorDto contractorDto;

    public ContractorServiceImpl(@Value("${contractor_url}") String contractorUrl, Retrofit retrofit) {
        this.contractorUrl = contractorUrl;
        this.contractorApi = retrofit.create(ContractorApi.class);
    }

//    @PostConstruct
//    public void test() {
//    }

    @Override
    public List<ContractorDto> getAll() {
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl);

        contractorDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ContractorDto>> call, Response<List<ContractorDto>> response) {
                if (response.isSuccessful()) {
                    contractorDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка ContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка ContractorDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ContractorDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка ContractorDto", throwable);
            }
        });

        return contractorDtoList;
    }

    @Override
    public ContractorDto getById(Long id) {
        Call<ContractorDto> contractorDtoCall = contractorApi.getById(contractorUrl, id);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    contractorDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра ContractorDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра ContractorDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра ContractorDto по id", throwable);
            }
        });

        return contractorDto;
    }

    @Override
    public void create(ContractorDto contractorDto) {
        Call<ContractorDto> contractorDtoCall = contractorApi.create(contractorUrl, contractorDto);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра ContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра ContractorDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра ContractorDto", throwable);
            }
        });
    }

    @Override
    public void update(ContractorDto contractorDto) {
        Call<ContractorDto> contractorDtoCall = contractorApi.update(contractorUrl, contractorDto);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра ContractorDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра ContractorDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра ContractorDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<ContractorDto> contractorDtoCall = contractorApi.deleteById(contractorUrl, id);

        contractorDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра ContractorDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра ContractorDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра ContractorDto", throwable);
            }
        });
    }
}

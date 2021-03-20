package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.api.ContractorApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ContractorServiceImpl implements ContractorService {

    private final String contractorUrl;

    private final ContractorApi contractorApi;

    private ContractorDto contractorDto;

    public ContractorServiceImpl(@Value("${contractor_url}") String contractorUrl, Retrofit retrofit) {
        this.contractorUrl = contractorUrl;
        this.contractorApi = retrofit.create(ContractorApi.class);
    }

    @Override
    public List<ContractorDto> getAll() {
//        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl);
//
//        contractorDtoListCall.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<List<ContractorDto>> call, Response<List<ContractorDto>> response) {
//                if (response.isSuccessful()) {
//                    contractorDtoList = response.body();
//                    log.info("Успешно выполнен запрос на получение списка ContractorDto");
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на получение списка ContractorDto - {}",
//                            response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ContractorDto>> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос списка ContractorDto", throwable);
//            }
//        });
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl);
        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto через getAll ");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractorDto: {IOException}", e);
        }
        return contractorDtoList;
    }

    @Override //create method
    public List<ContractorDto> getAllContractorDto() {
        List<ContractorDto> contractorDtoListString = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAllContractorDto(contractorUrl);
        try {
            contractorDtoListString = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto через getAllContractorDto ");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractorDto: {IOException}", e);
        }
        return contractorDtoListString;
    }

    @Override
    public List<ContractorDto> getAll(String searchTerm) {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.getAll(contractorUrl, searchTerm);
        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractorDto: {IOException}", e);
    }
        return contractorDtoList;
}

//добавил
    @Override
    public List<ContractorDto> searchContractor(Map<String, String> queryContractor) {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> contractorDtoListCall = contractorApi.searchContractor(contractorUrl, queryContractor);

        try {
            contractorDtoList = contractorDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка контрактов contractor");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение контрактов contractorDto: {IOException}", e);
        }
        return contractorDtoList;
    //return null;
    }

    @Override
    public List<ContractorDto> getAllLite() {
        List<ContractorDto> contractorDtoList = new ArrayList<>();
        Call<List<ContractorDto>> call = contractorApi.getAllLite(contractorUrl);
        try {
            contractorDtoList = call.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractorDto (Лёгкое ДТО)");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ContractorDto (Легкое ДТО): {}", e.getMessage());
        }
        return contractorDtoList;
    }

    @Override
    public ContractorDto getById(Long id) {
//        Call<ContractorDto> contractorDtoCall = contractorApi.getById(contractorUrl, id);
//
//        contractorDtoCall.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
//                if (response.isSuccessful()) {
//                    contractorDto = response.body();
//                    log.info("Успешно выполнен запрос на получение экземпляра ContractorDto по id= {}", id);
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра ContractorDto по id= {} - {}",
//                            id, response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос экземпляра ContractorDto по id", throwable);
//            }
//        });
        Call<ContractorDto> contractorDtoCall = contractorApi.getById(contractorUrl, id);

        try {
            contractorDto = contractorDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение ContractorDto : {IOException}", e);
        }

        return contractorDto;
    }

    @Override
    public void create(ContractorDto contractorDto) {
//        Call<ContractorDto> contractorDtoCall = contractorApi.create(contractorUrl, contractorDto);
//
//        contractorDtoCall.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
//                if (response.isSuccessful()) {
//                    log.info("Успешно выполнен запрос на создание экземпляра ContractorDto");
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра ContractorDto - {}",
//                            response.errorBody());
//                }
//            }
//            @Override
//            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра ContractorDto", throwable);
//            }
//        });

        Call<ContractorDto> contractorDtoCall = contractorApi.create(contractorUrl, contractorDto);

        try {
            contractorDtoCall.execute().body();
            log.info("Успешно выполнен запрос на добавление экземпляра ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра ContractorDto : {IOException}", e);
        }
    }

    @Override
    public void update(ContractorDto contractorDto) {
//        contractorDtoCall.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<ContractorDto> call, Response<ContractorDto> response) {
//                if (response.isSuccessful()) {
//                    log.info("Успешно выполнен запрос на обновление экземпляра ContractorDto");
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра ContractorDto - {}",
//                            response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ContractorDto> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра ContractorDto", throwable);
//            }
//        });
        Call<ContractorDto> contractorDtoCall = contractorApi.update(contractorUrl, contractorDto);

        try {
            contractorDtoCall.execute();
            log.info("Успешно выполнен запрос на изменение экземпляра ContractorDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на изменение экземпляра ContractorDto: {IOException}", e);
        }
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

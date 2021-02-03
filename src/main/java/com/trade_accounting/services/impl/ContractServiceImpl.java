package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.api.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final ContractApi contractApi;

    private final String contractUrl;

    @Autowired
    public ContractServiceImpl(@Value("${contract_url}") String contractUrl, Retrofit retrofit) {
        contractApi = retrofit.create(ContractApi.class);
        this.contractUrl = contractUrl;
    }

    @Override
    public List<ContractDto> getAll() {

        List<ContractDto> contractDtoList = new ArrayList<>();
        Call<List<ContractDto>> contractDtoListCall = contractApi.getAll(contractUrl);
        try {
            contractDtoList = contractDtoListCall.execute().body();
            Objects.requireNonNull(contractDtoList).forEach(contr -> contr.setDate(contr.getContractDate()));
            log.info("Успешно выполнен запрос на получение списка ContractDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractDto: {}", e);
        }
/*
        contractDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ContractDto>> call, Response<List<ContractDto>> response) {
                if (response.isSuccessful()) {
                    contractDtoList = response.body();
                    Objects.requireNonNull(contractDtoList).forEach(contr -> contr.setDate(contr.getContractDate()));
                    Objects.requireNonNull(contractDtoList).forEach(contr -> contr.getLegalDetailDto().
                            setDate(contr.getLegalDetailDto().getDateOfTheCertificate()));
                    log.info("Успешно выполнен запрос на получение списка ContractDto");
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение списка ContractDto: {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ContractDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение списка ContractDto: ", throwable);
            }
        });
*/
        return contractDtoList;
    }

    @Override
    public ContractDto getById(Long id) {
        ContractDto contractDto = new ContractDto();
        Call<ContractDto> contractDtoCall = contractApi.getById(contractUrl, id);
        try {
            contractDto = contractDtoCall.execute().body();
            Objects.requireNonNull(contractDto).setDate(contractDto.getContractDate());
            log.info("Успешно выполнен запрос на получение экземпляра ContractDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение ContractDto с id = {}: {}", id, e);
        }

        /*

        contractDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ContractDto> call, Response<ContractDto> response) {
                if (response.isSuccessful()) {
                    contractDto = response.body();
                    Objects.requireNonNull(contractDto).setDate(contractDto.getContractDate());
                    Objects.requireNonNull(contractDto).getLegalDetailDto().
                            setDate(contractDto.getLegalDetailDto().getDateOfTheCertificate());

                    log.info("Успешно выполнен запрос на получение экземпляра ContractDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение ContractDto с id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContractDto> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение ContractDto c id = {}: {}",
                        id, throwable);
            }
        });
        */
        return contractDto;
    }

    @Override
    public void create(ContractDto contractDto) {
        Call<Void> contractDtoCall = contractApi.create(contractUrl, contractDto);

        try {
            contractDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание нового экземпляра {}", contractDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}", contractDto, e);
        }

/*
        contractDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание нового экземпляра {}", contractDto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                            contractDto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}",
                        contractDto, throwable);
            }
        });
        */
    }

    @Override
    public void update(ContractDto contractDto) {
        Call<Void> contractDtoCall = contractApi.update(contractUrl, contractDto);

        try {
            contractDtoCall.execute().body();
            log.info("Успешно выполнен запрос на обновление {}", contractDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на обновление {}: {}", contractDto, e);
        }

        /*
        contractDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление {}", contractDto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                            contractDto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на обновление {}: {}",
                        contractDto, throwable);
            }
        });

         */
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contractDtoCall = contractApi.deleteById(contractUrl, id);

        try {
            contractDtoCall.execute().body();
            log.info("Успешно выполнен запрос на удаление ContractDto c id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление ContractDto c id = {}: {}",
                    id, e);
        }

        /*
        contractDtoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление ContractDto c id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на удаление ContractDto c id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на удаление ContractDto c id = {}: {}",
                        id, throwable);
            }
        });

         */
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.api.BankAccountApi;
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
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountApi bankAccountApi;

    private final String bankAccountUrl;

    private List<BankAccountDto> bankAccountDtoList;

    private BankAccountDto bankAccountDto;

    public BankAccountServiceImpl(@Value("${bank_account_url}") String bankAccountUrl, Retrofit retrofit) {

        this.bankAccountUrl = bankAccountUrl;
        bankAccountApi = retrofit.create(BankAccountApi.class);
    }

    @Override
    public List<BankAccountDto> getAll() {

        Call<List<BankAccountDto>> bankAccountDtoListCall = bankAccountApi.getAll(bankAccountUrl);

        bankAccountDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<BankAccountDto>> call, Response<List<BankAccountDto>> response) {
                if (response.isSuccessful()) {
                    bankAccountDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка BankAccountDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка BankAccountDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<BankAccountDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка BankAccountDto", throwable);
            }
        });

        return bankAccountDtoList;
    }

    @Override
    public BankAccountDto getById(Long id) {

        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.getById(bankAccountUrl, id);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BankAccountDto> call, Response<BankAccountDto> response) {
                if (response.isSuccessful()) {
                    bankAccountDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра BankAccountDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра BankAccountDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BankAccountDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра BankAccountDto по id", throwable);
            }
        });
        return bankAccountDto;
    }

    @Override
    public void create(BankAccountDto dto) {

        Call<Void> bankAccountDtoCall = bankAccountApi.create(bankAccountUrl, dto);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра BankAccountDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра BankAccountDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра BankAccountDto", throwable);
            }
        });
    }

    @Override
    public void update(BankAccountDto dto) {

        Call<Void> bankAccountDtoCall = bankAccountApi.update(bankAccountUrl, dto);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра BankAccountDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра BankAccountDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра BankAccountDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> bankAccountDtoCall = bankAccountApi.deleteById(bankAccountUrl, id);

        bankAccountDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра BankAccountDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра BankAccountDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра BankAccountDto", throwable);
            }
        });
    }
}

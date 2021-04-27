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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountApi bankAccountApi;
    private final String bankAccountUrl;

    public BankAccountServiceImpl(@Value("${bank_account_url}") String bankAccountUrl, Retrofit retrofit) {

        this.bankAccountUrl = bankAccountUrl;
        bankAccountApi = retrofit.create(BankAccountApi.class);
    }

    @Override
    public BankAccountDto getByBic(String uniqBic) {
        BankAccountDto bankAccountDto = null;
        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.getByBic(bankAccountUrl, uniqBic);

        try {
            bankAccountDto = bankAccountDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра BankAccountDto по bic = {}", uniqBic);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра BankAccountDto по bic= {} - {}",
                    uniqBic, e);
        }

        return bankAccountDto;
    }

    @Override
    public List<String> getBankUniqueBic() {
        List<String> bankUniqueBic = new ArrayList<>();
        Call<List<String>> bankAccountDtoListCall = bankAccountApi.getBankUniqueBic(bankAccountUrl);

        try {
            bankUniqueBic.addAll(bankAccountDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка BankAccountDto с уникальным биком");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка BankAccountDto - {}", e);
        }

        return bankUniqueBic;
    }

    @Override
    public List<BankAccountDto> getAll() {

        List<BankAccountDto> bankAccountDtoList = new ArrayList<>();
        Call<List<BankAccountDto>> bankAccountDtoListCall = bankAccountApi.getAll(bankAccountUrl);

        try {
            bankAccountDtoList.addAll(bankAccountDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка BankAccountDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка BankAccountDto - {}", e);
        }

        return bankAccountDtoList;
    }


    /*@Override
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
    }*/

    @Override
    public BankAccountDto getById(Long id) {

        BankAccountDto bankAccountDto = null;
        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.getById(bankAccountUrl, id);

        try {
            bankAccountDto = bankAccountDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра BankAccountDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра BankAccountDto по id= {} - {}",
                    id, e);
        }

        return bankAccountDto;
    }


    /*@Override
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
    }*/

    @Override
    public void create(BankAccountDto bankAccountDto) {

        Call<Void> bankAccountDtoCall = bankAccountApi.create(bankAccountUrl, bankAccountDto);

        try {
            bankAccountDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра BankAccountDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра BankAccountDto - {}",
                    e);
        }
    }


    /*@Override
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
    }*/


    @Override
    public void update(BankAccountDto bankAccountDto) {

        Call<Void> bankAccountDtoCall = bankAccountApi.update(bankAccountUrl, bankAccountDto);

        try {
            bankAccountDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра BankAccountDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра BankAccountDto - {}",
                    e);
        }

    }


    /*@Override
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
    }*/

    @Override
    public void deleteById(Long id) {

        Call<Void> bankAccountDtoCall = bankAccountApi.deleteById(bankAccountUrl, id);

        try {
            bankAccountDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра BankAccountDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра BankAccountDto с id= {} - {}",
                    id, e);
        }
    }

    /*@Override
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
    }*/

}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.api.CompanyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyApi companyApi;
    private final String companyUrl;

    public CompanyServiceImpl(@Value("${company_url}") String companyUrl, Retrofit retrofit) {
        this.companyUrl = companyUrl;
        companyApi = retrofit.create(CompanyApi.class);
    }

    @Override
    public List<CompanyDto> getAll() {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll(companyUrl);

        try {
            companyDtoList.addAll(Objects.requireNonNull(companyDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка CompanyDto - {}", e);
        }

        /*Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll(companyUrl);

        companyDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CompanyDto>> call, Response<List<CompanyDto>> response) {
                if (response.isSuccessful()) {
                    companyDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка CompanyDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка CompanyDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<CompanyDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка CompanyDto", throwable);
            }
        });*/

        return companyDtoList;
    }

    @Override
    public List<CompanyDto> search(Map<String, String> query) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Call<List<CompanyDto>> companyDtoListCall = companyApi.search(companyUrl, query);

        try {
            companyDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка CompanyDto - ", e);
        }

        return companyDtoList;
    }

    @Override
    public CompanyDto getById(Long id) {
        Call<CompanyDto> companyDtoCall = companyApi.getById(companyUrl, id);
        CompanyDto companyDto = null;

        try {
            companyDto = companyDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра CompanyDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра CompanyDto по id= {} - {}",
                    id, e);
        }

        /*companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (response.isSuccessful()) {
                    companyDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра CompanyDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра CompanyDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра CompanyDto по id", throwable);
            }
        });*/

        return companyDto;
    }

    @Override
    public CompanyDto getByEmail(String email) {
        Call<CompanyDto> companyDtoCall = companyApi.getByEmail(companyUrl, email);
        CompanyDto companyDto = null;

        try {
            companyDto = companyDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра CompanyDto по email= {}", email);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра CompanyDto по id= {} - {}",
                    email, e);
        }

        /*companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CompanyDto> call, Response<CompanyDto> response) {
                if (response.isSuccessful()) {
                    companyDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра CompanyDto по email= {}", email);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра CompanyDto по id= {} - {}",
                            email, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CompanyDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра CompanyDto по email", throwable);
            }
        });*/

        return companyDto;
    }

    @Override
    public void create(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.create(companyUrl, companyDto);

        try {
            companyDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра CompanyDto - {}", e);
        }

        /*companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра CompanyDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра CompanyDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра CompanyDto", throwable);
            }
        });*/
    }

    @Override
    public void update(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.update(companyUrl, companyDto);

        try {
            companyDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра CompanyDto - {}", e);
        }

        /*companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра CompanyDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра CompanyDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра CompanyDto", throwable);
            }
        });*/
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> companyDtoCall = companyApi.deleteById(companyUrl, id);

        try {
            companyDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра CompanyDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра CompanyDto с id= {} - {}", e);
        }

        /*companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра CompanyDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра CompanyDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра CompanyDto", throwable);
            }
        });*/
    }
}

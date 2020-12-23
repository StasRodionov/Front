package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.api.CompanyApi;
import com.vaadin.flow.component.Synchronize;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyApi companyApi;

    private final String companyUrl;

    private List<CompanyDto> companyDtoList = new ArrayList<>();

    private CompanyDto companyDto;

    public CompanyServiceImpl(@Value("${company_url}") String companyUrl, Retrofit retrofit) {
        this.companyUrl = companyUrl;
        companyApi = retrofit.create(CompanyApi.class);
    }

    @Override
    public List<CompanyDto> getAll() {
        Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll(companyUrl);
        final Queue<Object> queue = new LinkedList<>();

        try {
            companyDtoListCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<CompanyDto>> call, Response<List<CompanyDto>> response) {
                    if (response.isSuccessful()) {
                        synchronized (queue) {
                            companyDtoList = response.body();
                            log.info("Успешно выполнен запрос на получение списка CompanyDto");
                            queue.add(companyDtoList);
                            queue.notify();
                        }
                    } else {
                        log.error("Произошла ошибка при выполнении запроса на получение списка CompanyDto - {}",
                                response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<List<CompanyDto>> call, Throwable throwable) {
                    log.error("Произошла ошибка при получении ответа на запрос списка CompanyDto", throwable);
                }
            });

            synchronized (queue) {
                while (queue.size() == 0) {
                    queue.wait();
                }
                return companyDtoList;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CompanyDto getById(Long id) {
        Call<CompanyDto> companyDtoCall = companyApi.getById(companyUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
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
        });

        return companyDto;
    }

    @Override
    public void create(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.create(companyUrl, companyDto);

        companyDtoCall.enqueue(new Callback<>() {
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
        });
    }

    @Override
    public void update(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.update(companyUrl, companyDto);

        companyDtoCall.enqueue(new Callback<>() {
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
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> companyDtoCall = companyApi.deleteById(companyUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
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
        });
    }

   /* @PostConstruct
    public void test(){

        getById(1L);
    }*/
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.DepartmentDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.api.DepartmentApi;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentApi departmentApi;

    private final String departmentUrl;

    private List<DepartmentDto> departmentDtoList;

    private DepartmentDto departmentDto;

    public DepartmentServiceImpl(@Value("${department_url}") String departmentUrl, Retrofit retrofit) {
        this.departmentUrl = departmentUrl;
        this.departmentApi = retrofit.create(DepartmentApi.class);
    }

//    @PostConstruct
//    public void init() {
//        getAll();
//        getById(1L);
//        create(new DepartmentDto());
//        deleteById(2L);
//    }

    @Override
    public List<DepartmentDto> getAll() {
        Call<List<DepartmentDto>> departmentDtoListCall = departmentApi.getAll(departmentUrl);

        departmentDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DepartmentDto>> call, Response<List<DepartmentDto>> response) {
                if (response.isSuccessful()) {
                    departmentDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка DepartmentDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка DepartmentDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<DepartmentDto>> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос списка DepartmentDto", t);
            }
        });

        return departmentDtoList;
    }

    @Override
    public DepartmentDto getById(Long id) {
        Call<DepartmentDto> departmentDtoCall = departmentApi.getById(departmentUrl, id);

        departmentDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DepartmentDto> call, Response<DepartmentDto> response) {
                if (response.isSuccessful()) {
                    departmentDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра DepartmentDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра DepartmentDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<DepartmentDto> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра DepartmentDto по id", t);
            }
        });

        return departmentDto;
    }

    @Override
    public void create(DepartmentDto departmentDto) {
        Call<Void> call = departmentApi.create(departmentUrl, departmentDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра DepartmentDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра DepartmentDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра DepartmentDto", t);
            }
        });
    }

    @Override
    public void update(DepartmentDto departmentDto) {
        Call<Void> call = departmentApi.update(departmentUrl, departmentDto);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра DepartmentDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра DepartmentDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра DepartmentDto", t);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> call = departmentApi.deleteById(departmentUrl, id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра DepartmentDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра DepartmentDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра DepartmentDto", t);
            }
        });
    }
}

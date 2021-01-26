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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentApi departmentApi;
    private final String departmentUrl;

    public DepartmentServiceImpl(@Value("${department_url}") String departmentUrl, Retrofit retrofit) {
        this.departmentUrl = departmentUrl;
        departmentApi = retrofit.create(DepartmentApi.class);
    }

    @Override
    public List<DepartmentDto> getAll() {

        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        Call<List<DepartmentDto>> departmentDtoListCall = departmentApi.getAll(departmentUrl);

        try {
            departmentDtoList.addAll(departmentDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка DepartmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка DepartmentDto - {}",
                    e);
        }

        return departmentDtoList;
    }


    /*@Override
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
    }*/

    @Override
    public DepartmentDto getById(Long id) {

        DepartmentDto departmentDto = null;
        Call<DepartmentDto> departmentDtoCall = departmentApi.getById(departmentUrl, id);

        try {
            departmentDto = departmentDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра DepartmentDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра DepartmentDto по id= {} - {}",
                    id, e);
        }

        return departmentDto;
    }


    /*@Override
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
    }*/

    @Override
    public void create(DepartmentDto departmentDto) {

        Call<Void> departmentDtoCall = departmentApi.create(departmentUrl, departmentDto);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра DepartmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра DepartmentDto - {}",
                    e);
        }
    }

    /*@Override
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
    }*/

    @Override
    public void update(DepartmentDto departmentDto) {

        Call<Void> departmentDtoCall = departmentApi.update(departmentUrl, departmentDto);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра DepartmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра DepartmentDto - {}",
                    e);
        }
    }


    /*@Override
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
    }*/


    @Override
    public void deleteById(Long id) {

        Call<Void> departmentDtoCall = departmentApi.deleteById(departmentUrl, id);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра DepartmentDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра DepartmentDto с id= {} - {}",
                    id, e);
        }
    }


    /*@Override
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
    }*/

}

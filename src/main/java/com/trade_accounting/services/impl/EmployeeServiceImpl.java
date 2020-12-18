package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.api.EmployeeApi;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeApi employeeApi;
    private final String employeeUrl;
    private List<EmployeeDto> employeeDtoList;
    private EmployeeDto employeeDto;

    public EmployeeServiceImpl(@Value("${employee_url}") String employeeUrl, Retrofit retrofit) {
        this.employeeUrl = employeeUrl;
        employeeApi = retrofit.create(EmployeeApi.class);
    }

    @Override
    public List<EmployeeDto> getAll() {

        Call<List<EmployeeDto>> employeeDtoListCall = employeeApi.getAll(employeeUrl);

        employeeDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<EmployeeDto>> call, Response<List<EmployeeDto>> response) {
                if (response.isSuccessful()) {
                    employeeDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка EmployeeDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка EmployeeDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка EmployeeDto", throwable);
            }
        });
        return employeeDtoList;
    }

    @Override
    public EmployeeDto getById(Long id) {

        Call<EmployeeDto> employeeDtoCall = employeeApi.getById(employeeUrl, id);

        employeeDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EmployeeDto> call, Response<EmployeeDto> response) {
                if (response.isSuccessful()) {
                    employeeDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра EmployeeDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра EmployeeDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<EmployeeDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра EmployeeDto по id", throwable);
            }
        });
        return employeeDto;
    }

    @Override
    public void create(EmployeeDto employeeDto) {

        Call<Void> employeeDtoCall = employeeApi.create(employeeUrl, employeeDto);

        employeeDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра EmployeeDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра EmployeeDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра EmployeeDto", throwable);
            }
        });

    }

    @Override
    public void update(EmployeeDto employeeDto) {

        Call<Void> employeeDtoCall = employeeApi.update(employeeUrl, employeeDto);

        employeeDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра EmployeeDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра EmployeeDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра EmployeeDto", throwable);
            }
        });

    }

    @Override
    public void deleteById(Long id) {

        Call<Void> employeeDtoCall = employeeApi.deleteById(employeeUrl, id);

        employeeDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра EmployeeDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра EmployeeDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра EmployeeDto", throwable);
            }
        });

    }
}

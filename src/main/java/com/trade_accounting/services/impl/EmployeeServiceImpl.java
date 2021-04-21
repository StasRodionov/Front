package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeApi employeeApi;
    private final String employeeUrl;

    public EmployeeServiceImpl(@Value("${employee_url}") String employeeUrl, Retrofit retrofit) {
        this.employeeUrl = employeeUrl;
        employeeApi = retrofit.create(EmployeeApi.class);
    }

    @Override
    public List<EmployeeDto> findBySearch(String search) {
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        Call<List<EmployeeDto>> companyDtoListCall = employeeApi.findBySearch(employeeUrl, search.toLowerCase());

        try {
            employeeDtos = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }

        return employeeDtos;
    }

    public Long getRowsCount(Map<String, String> query) {
        Call<Long> getRow = employeeApi.getRowCount(employeeUrl, query);
        Long countRow = 0L;
        try {
            countRow = getRow.execute().body();
            log.info("Успешно выполнен запрос");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }
        return countRow;
    }

    @Override
    public List<EmployeeDto> getAll() {

        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        Call<List<EmployeeDto>> employeeDtoListCall = employeeApi.getAll(employeeUrl);

        try {
            employeeDtoList = employeeDtoListCall.execute().body();

            log.info("Успешно выполнен запрос на получение списка EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка EmployeeDto");
        }
/*        employeeDtoListCall.enqueue(new Callback<>() {
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
 */
        return employeeDtoList;
    }

    @Override
    public List<EmployeeDto> search(Map<String, String> query) {
        List<EmployeeDto> companyDtoList = new ArrayList<>();
        Call<List<EmployeeDto>> companyDtoListCall = employeeApi.search(employeeUrl, query);

        try {
            companyDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }

        return companyDtoList;
    }

    @Override
    public List<EmployeeDto> getPage(Map<String, String> filterParams, Map<String, String> sortParams, int page, int count) {
        List<EmployeeDto> companyDtoList = new ArrayList<>();
        Call<List<EmployeeDto>> companyDtoListCall = employeeApi.getPage(employeeUrl, sortParams, filterParams, page, count);

        try {
            companyDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка EmployeeDto по фильтру");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }

        return companyDtoList;
    }

    @Override
    public EmployeeDto getById(Long id) {

        EmployeeDto employeeDto = new EmployeeDto();

        Call<EmployeeDto> employeeDtoCall = employeeApi.getById(employeeUrl, id);
        try {
            employeeDto = employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра EmployeeDto");
        }

/*        employeeDtoCall.enqueue(new Callback<>() {
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

 */
        return employeeDto;
    }

    @Override
    public void create(EmployeeDto employeeDto) {

        Call<Void> employeeDtoCall = employeeApi.create(employeeUrl, employeeDto);

        try {
            employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра EmployeeDto");
        }

/*        employeeDtoCall.enqueue(new Callback<>() {
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

 */

    }

    @Override
    public void update(EmployeeDto employeeDto) {

        Call<Void> employeeDtoCall = employeeApi.update(employeeUrl, employeeDto);

        try {
            employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос на обновление экземпляра EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра EmployeeDto");
        }

/*        employeeDtoCall.enqueue(new Callback<>() {
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
 */
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> employeeDtoCall = employeeApi.deleteById(employeeUrl, id);

        try {
            employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос на удаление экземпляра EmployeeDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра EmployeeDto");
        }

/*        employeeDtoCall.enqueue(new Callback<>() {
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
 */
    }

    @Override
    public EmployeeDto getPrincipal() {
        EmployeeDto employeeDto = new EmployeeDto();
        Call<EmployeeDto> employeeDtoCall = employeeApi.getPrincipal(employeeUrl);
        try {
            employeeDto = employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос информации о работнике");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса информации о работнике");
        }
        return employeeDto;
    }
}

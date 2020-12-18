package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.api.RoleApi;
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
public class RoleServiceImpl implements RoleService {

    private final RoleApi roleApi;

    private final String roleUrl;

    private List<RoleDto> roleDtoList;

    private RoleDto roleDto;

    public RoleServiceImpl(@Value("${role_url}") String roleUrl, Retrofit retrofit) {
        this.roleUrl = roleUrl;
        roleApi = retrofit.create(RoleApi.class);
    }

    @Override
    public List<RoleDto> getAll() {
        Call<List<RoleDto>> roleDtoListCall = roleApi.getAll(roleUrl);

        roleDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<RoleDto>> call, Response<List<RoleDto>> response) {
                if (response.isSuccessful()) {
                    roleDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка RoleDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка RoleDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<RoleDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка RoleDto", throwable);
            }
        });

        return roleDtoList;
    }

    @Override
    public RoleDto getById(Long id) {
        Call<RoleDto> roleDtoCall = roleApi.getById(roleUrl, id);

        roleDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RoleDto> call, Response<RoleDto> response) {
                if (response.isSuccessful()) {
                    roleDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра RoleDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра RoleDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RoleDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра RoleDto по id", throwable);
            }
        });

        return roleDto;
    }

    @Override
    public void create(RoleDto roleDto) {
        Call<Void> roleDtoCall = roleApi.create(roleUrl, roleDto);

        roleDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра RoleDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра RoleDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра RoleDto", throwable);
            }
        });
    }

    @Override
    public void update(RoleDto roleDto) {
        Call<Void> companyDtoCall = roleApi.update(roleUrl, roleDto);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра RoleDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра RoleDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра RoleDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> companyDtoCall = roleApi.deleteById(roleUrl, id);

        companyDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра RoleDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра RoleDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра RoleDto", throwable);
            }
        });
    }
}

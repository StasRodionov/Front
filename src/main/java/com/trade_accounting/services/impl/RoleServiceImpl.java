package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.api.RoleApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleApi roleApi;

    private final String roleUrl;

    private List<RoleDto> roleDtoList = new ArrayList<>();

    private RoleDto roleDto = new RoleDto();

    public RoleServiceImpl(@Value("${role_url}") String roleUrl, Retrofit retrofit) {
        this.roleUrl = roleUrl;
        roleApi = retrofit.create(RoleApi.class);
    }

    @Override
    public List<RoleDto> getAll() {
        Call<List<RoleDto>> roleGetAllCall = roleApi.getAll(roleUrl);
        try {
            roleDtoList = roleGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка RoleDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка RoleDto");
        }
        return roleDtoList;
    }

    @Override
    public RoleDto getById(Long id) {
        Call<RoleDto> roleGetCall = roleApi.getById(roleUrl, id);
        try {
            roleDto = roleGetCall.execute().body();
            log.info("Успешно выполнен запрос на получение RoleDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение RoleDto - {}", e);
        }
        return roleDto;
    }

    @Override
    public void create(RoleDto roleDto) {
        Call<Void> roleCall = roleApi.create(roleUrl, roleDto);
        try {
            roleCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при создании RoleDto - {}", e);
        }
    }

    @Override
    public void update(RoleDto roleDto) {
        Call<Void> roleUpdateCall = roleApi.update(roleUrl, roleDto);
        try {
            roleUpdateCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при обновлении RoleDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> roleDeleteCall = roleApi.deleteById(roleUrl, id);
        try {
            roleDeleteCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при удалении ProductDto - {}", e);
        }
    }
}
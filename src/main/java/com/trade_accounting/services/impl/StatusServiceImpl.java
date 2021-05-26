package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.models.dto.StatusDto;
import com.trade_accounting.services.interfaces.StatusService;
import com.trade_accounting.services.interfaces.api.StatusApi;
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
public class StatusServiceImpl implements StatusService {

    private final StatusApi statusApi;

    private final String statusUrl;

    private List<StatusDto> statusDtoList = new ArrayList<>();

    private StatusDto statusDto = new StatusDto();

    public StatusServiceImpl(@Value("${status_url}") String statusUrl, Retrofit retrofit) {
        this.statusApi = retrofit.create(StatusApi.class);
        this.statusUrl = statusUrl;
    }


    @Override
    public List<StatusDto> getAll() {
        Call<List<StatusDto>> statusGetAllCall = statusApi.getAll(statusUrl);
        try {
            statusDtoList = statusGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка statusDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка statusDto");
        }
        return statusDtoList;
    }

    @Override
    public StatusDto getById(Long id) {
        Call<StatusDto> statusGetCall = statusApi.getById(statusUrl, id);
        try {
            statusDto = statusGetCall.execute().body();
            log.info("Успешно выполнен запрос на получение statusDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение statusDto - {}", e);
        }
        return statusDto;
    }

}

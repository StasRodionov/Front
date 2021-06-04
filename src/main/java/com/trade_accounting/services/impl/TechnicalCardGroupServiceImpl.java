package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TechnicalCardGroupDto;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.trade_accounting.services.interfaces.api.TechnicalCardGroupApi;
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
public class TechnicalCardGroupServiceImpl implements TechnicalCardGroupService {

    private final String technicalCardGroupUrl;

    private final TechnicalCardGroupApi technicalCardGroupApi;

    private List<TechnicalCardGroupDto> technicalCardGroupDtoList = new ArrayList<>();

    private TechnicalCardGroupDto technicalCardGroupDto = new TechnicalCardGroupDto();

    public TechnicalCardGroupServiceImpl(@Value("${technical_card_group_url}") String technicalCardGroupUrl, Retrofit retrofit) {
        this.technicalCardGroupUrl = technicalCardGroupUrl;
        technicalCardGroupApi = retrofit.create(TechnicalCardGroupApi.class);
    }

    @Override
    public List<TechnicalCardGroupDto> getAll() {
        Call<List<TechnicalCardGroupDto>> technicalCardGroupGetAll = technicalCardGroupApi.getAll(technicalCardGroupUrl);
        try {
            technicalCardGroupDtoList = technicalCardGroupGetAll.execute().body();
            log.info("Успешно выполнен запрос на получение списка TechnicalCardGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка TechnicalCardGroupDto - {}", e.getMessage());
        }
        return technicalCardGroupDtoList;
    }

    @Override
    public TechnicalCardGroupDto getById(Long id) {
        Call<TechnicalCardGroupDto> technicalCardGroupDtoGetCall = technicalCardGroupApi.getById(technicalCardGroupUrl, id);
        try {
            technicalCardGroupDto = technicalCardGroupDtoGetCall.execute().body();
            log.info("Успешно выполнен запрос на получение TechnicalCardGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение TechnicalCardGroupDto - {}", e.getMessage());
        }
        return technicalCardGroupDto;
    }

    @Override
    public void create(TechnicalCardGroupDto technicalCardGroupDto) {
        Call<Void> technicalCardGroupCreateCall = technicalCardGroupApi.create(technicalCardGroupUrl, technicalCardGroupDto);
        try {
            technicalCardGroupCreateCall.execute();
            log.info("Успешно выполнен запрос create TechnicalCardGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при создании TechnicalCardGroupDto - {}", e);
        }
    }

    @Override
    public void update(TechnicalCardGroupDto technicalCardGroupDto) {
        Call<Void> technicalCardGroupUpdateCall = technicalCardGroupApi.update(technicalCardGroupUrl, technicalCardGroupDto);
        try {
            technicalCardGroupUpdateCall.execute();
            log.info("Успешно выполнен запрос update TechnicalCardGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при обновлении TechnicalCardGroupDto - {}", e.getMessage());
        }

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> technicalCardGroupDeleteCall = technicalCardGroupApi.deleteById(technicalCardGroupUrl, id);
        log.info("Отправлен запрос на удаление TechnicalCardGroupDto с id = {}", id);
        try {
            technicalCardGroupDeleteCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при удалении TechnicalCardGroupDto - {}", e.getMessage());
        }
    }

}

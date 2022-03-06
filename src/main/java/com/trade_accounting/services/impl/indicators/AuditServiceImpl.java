package com.trade_accounting.services.impl.indicators;

import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.services.api.indicators.AuditApi;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.indicators.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuditServiceImpl implements AuditService {
    private final AuditApi auditApi;
    private final String auditUrl;
    private final CallExecuteService<AuditDto> callExecuteService;

    public AuditServiceImpl(Retrofit retrofit, @Value("${audit_url}") String auditUrl, CallExecuteService<AuditDto> callExecuteService) {
        this.auditApi = retrofit.create(AuditApi.class);
        this.auditUrl = auditUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AuditDto> getAll() {
        Call<List<AuditDto>> listCall = auditApi.getAll(auditUrl);
        return callExecuteService.callExecuteBodyList(listCall, AuditDto.class);
    }

    @Override
    public AuditDto getById(Long id) {
        Call<AuditDto> call = auditApi.getById(auditUrl, id);
        return callExecuteService.callExecuteBodyById(call, AuditDto.class, id);
    }

    @Override
    public AuditDto create(AuditDto auditDto) {
        Call<AuditDto> call = auditApi.create(auditUrl, auditDto);
        try {
            auditDto = call.execute().body();
            log.info("Успешно выполнен запрос на создание Audit");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение AuditDto - {}", e);
        }
        return auditDto;
    }

    @Override
    public void update(AuditDto auditDto) {
        Call<Void> call = auditApi.update(auditUrl, auditDto);
        try{
            call.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра Audit");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра AuditDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> call = auditApi.deleteById(auditUrl, id);
        callExecuteService.callExecuteBodyDelete(call, AuditDto.class, id);
    }

    @Override
    public List<AuditDto> quickSearch(String text) {
        List<AuditDto> auditDtos = new ArrayList<>();
        Call<List<AuditDto>> call = auditApi
                .quickSearch(auditUrl, text.toLowerCase());

        try {
            auditDtos = call.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка событий");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка событий: ", e);
        }
        return auditDtos;
    }

    @Override
    public List<AuditDto> searchByFilter(Map<String, String> queryOperations) {
        List<AuditDto> auditDtos = new ArrayList<>();
        Call<List<AuditDto>> callOperation = auditApi.searchByFilter(auditUrl, queryOperations);
        try {
            auditDtos = callOperation.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение событий по фильтру {}", auditDtos);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение событий {IOException}", e);
        }
        return auditDtos;
    }
}
package com.trade_accounting.components.apps.impl.production;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.production.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.production.TechnicalOperationsService;
import com.trade_accounting.services.api.production.TechnicalOperationsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TechnicalOperationsServiceImpl implements TechnicalOperationsService {

    private final String technicalOperationsUrl;
    private final TechnicalOperationsApi technicalOperationsApi;
    private final CallExecuteService<TechnicalOperationsDto> dtoCallExecuteService;
    private List<TechnicalOperationsDto> technicalOperationsList = new ArrayList<>();

    public TechnicalOperationsServiceImpl(@Value("${technical_operations_url}") String technicalOperationsUrl, Retrofit retrofit, CallExecuteService<TechnicalOperationsDto> dtoCallExecuteService) {
        this.technicalOperationsUrl = technicalOperationsUrl;
        this.technicalOperationsApi = retrofit.create(TechnicalOperationsApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalOperationsDto> getAll() {
        Call<List<TechnicalOperationsDto>> technicalOperationsGetAll = technicalOperationsApi.getAll(technicalOperationsUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalOperationsGetAll, TechnicalOperationsDto.class);
    }

    @Override
    public TechnicalOperationsDto getById(Long id) {
        Call<TechnicalOperationsDto> technicalOperationsDtoGetCall = technicalOperationsApi.getById(technicalOperationsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalOperationsDtoGetCall, TechnicalOperationsDto.class, id);
    }

    @Override
    public void create(TechnicalOperationsDto dto) {
        Call<Void> technicalOperationsCreateCall = technicalOperationsApi.create(technicalOperationsUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(technicalOperationsCreateCall, TechnicalOperationsDto.class);
    }

    @Override
    public void update(TechnicalOperationsDto dto) {
        Call<Void> technicalOperationsUpdateCall = technicalOperationsApi.update(technicalOperationsUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(technicalOperationsUpdateCall, TechnicalOperationsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> technicalOperationsDeleteCall = technicalOperationsApi.deleteById(technicalOperationsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(technicalOperationsDeleteCall, TechnicalOperationsDto.class, id);
    }

    @Override
    public List<TechnicalOperationsDto> search(String query) {
        Call<List<TechnicalOperationsDto>> technicalOperationsListCall = technicalOperationsApi.search(technicalOperationsUrl, query);
        try {
            technicalOperationsList = technicalOperationsListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка TechnicalOperationsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка TechnicalOperationsDto - ", e);
        }
        return technicalOperationsList;
    }

    @Override
    public List<TechnicalOperationsDto> searchTechnicalOperations(Map<String, String> queryTechnicalOperations) {
        List<TechnicalOperationsDto> technicalOperationsDtoList = new ArrayList<>();
        Call<List<TechnicalOperationsDto>> technicalOperationsDtoListCall = technicalOperationsApi.searchContractor(technicalOperationsUrl, queryTechnicalOperations);
        try {
            technicalOperationsDtoList = technicalOperationsDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка технических операции");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка технических операции: {IOException}", e);
        }
        return technicalOperationsDtoList;
    }

    @Override
    public void moveToIsRecyclebin(Long id) {
        Call<Void> dtoCall = technicalOperationsApi.moveToIsRecyclebin(technicalOperationsUrl, id);
        dtoCallExecuteService.callExecuteBodyMoveToIsRecyclebin(dtoCall, TechnicalOperationsDto.class, id);
    }

    @Override
    public void restoreFromIsRecyclebin(Long id) {
        Call<Void> dtoCall = technicalOperationsApi.restoreFromIsRecyclebin(technicalOperationsUrl, id);
        dtoCallExecuteService.callExecuteBodyRestoreFromIsRecyclebin(dtoCall, TechnicalOperationsDto.class, id);

    }
}

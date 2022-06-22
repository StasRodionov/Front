package com.trade_accounting.components.apps.impl.production;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.production.TechnicalProcessDto;
import com.trade_accounting.services.interfaces.production.TechnicalProcessService;
import com.trade_accounting.services.api.production.TechnicalProcessApi;
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
public class TechnicalProcessServiceImpl implements TechnicalProcessService {

    private final String technicalProcessUrl;
    private final TechnicalProcessApi technicalProcessApi;
    private final CallExecuteService<TechnicalProcessDto> dtoCallExecuteService;

    public TechnicalProcessServiceImpl(@Value("${technical_process_url}") String technicalProcessUrl, Retrofit retrofit, CallExecuteService<TechnicalProcessDto> dtoCallExecuteService) {
        this.technicalProcessUrl = technicalProcessUrl;
        this.technicalProcessApi = retrofit.create(TechnicalProcessApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TechnicalProcessDto> getAll() {
        Call<List<TechnicalProcessDto>> technicalProcessGetAll = technicalProcessApi.getAll(technicalProcessUrl);
        return dtoCallExecuteService.callExecuteBodyList(technicalProcessGetAll, TechnicalProcessDto.class);
    }

    @Override
    public TechnicalProcessDto getById(long id) {
        Call<TechnicalProcessDto> technicalProcessDtoGetCall = technicalProcessApi.getById(technicalProcessUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(technicalProcessDtoGetCall, TechnicalProcessDto.class, id);
    }

    @Override
    public void create(TechnicalProcessDto dto) {
        Call<Void> technicalProcessDtoCreateCall = technicalProcessApi.create(technicalProcessUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(technicalProcessDtoCreateCall, TechnicalProcessDto.class);
    }

    @Override
    public void update(TechnicalProcessDto dto) {
        Call<Void> technicalProcessDtoUpdateCall = technicalProcessApi.update(technicalProcessUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(technicalProcessDtoUpdateCall, TechnicalProcessDto.class);
    }

    @Override
    public void deleteById(long id) {
        Call<Void> technicalProcessDtoDeleteCall = technicalProcessApi.deleteById(technicalProcessUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(technicalProcessDtoDeleteCall, TechnicalProcessDto.class, id);
    }

    @Override
    public List<TechnicalProcessDto> search(String query) {
        List<TechnicalProcessDto> technicalProcessDtoList = new ArrayList<>();
        Call<List<TechnicalProcessDto>> technicalProcessListCall = technicalProcessApi.search(technicalProcessUrl, query);
        try {
            technicalProcessDtoList = technicalProcessListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TechnicalProcess");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка TechnicalProcess - ", e);
        }
        return technicalProcessDtoList;
    }

    @Override
    public List<TechnicalProcessDto> searchTechnicalProcess(Map<String, String> queryTechnicalProcess) {
        List<TechnicalProcessDto> technicalProcessDtoList = new ArrayList<>();
        Call<List<TechnicalProcessDto>> listCall = technicalProcessApi.searchContractor(technicalProcessUrl, queryTechnicalProcess);
        try {
            technicalProcessDtoList = listCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TechnicalProcess");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка TechnicalProcess - ", e);
        }
        return technicalProcessDtoList;
    }
}

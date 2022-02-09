package com.trade_accounting.services.impl.production;

import com.trade_accounting.models.dto.production.StagesProductionDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.production.StagesProductionService;
import com.trade_accounting.services.api.production.StagesProductionApi;
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
public class StagesProductionServiceImpl implements StagesProductionService {

    private final String stagesProductionUrl;
    private final StagesProductionApi stagesProductionApi;
    private final StagesProductionDto stagesProductionDto = new StagesProductionDto();
    private final CallExecuteService<StagesProductionDto> dtoCallExecuteService;


    public StagesProductionServiceImpl(@Value("${stage_production_url}") String stagesProductionUrl, Retrofit retrofit, CallExecuteService<StagesProductionDto> dtoCallExecuteService) {
        this.stagesProductionUrl = stagesProductionUrl;
        this.stagesProductionApi = retrofit.create(StagesProductionApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<StagesProductionDto> getALl() {
        Call<List<StagesProductionDto>> stagesProductionGetALL = stagesProductionApi.getAll(stagesProductionUrl);
        return dtoCallExecuteService.callExecuteBodyList(stagesProductionGetALL, StagesProductionDto.class);
    }

    @Override
    public StagesProductionDto getById(long id) {
        Call<StagesProductionDto> stagesProductionDtoGetCall = stagesProductionApi.getById(stagesProductionUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(stagesProductionDtoGetCall, StagesProductionDto.class, id);
    }

    @Override
    public void create(StagesProductionDto dto) {
        Call<Void> stagesProductionDtoCreateCall = stagesProductionApi.create(stagesProductionUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(stagesProductionDtoCreateCall, StagesProductionDto.class);
    }

    @Override
    public void update(StagesProductionDto dto) {
        Call<Void> stagesProductionDtoUpdateCall = stagesProductionApi.update(stagesProductionUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(stagesProductionDtoUpdateCall, StagesProductionDto.class);
    }

    @Override
    public void deleteById(long id) {
        Call<Void> stagesProductionDtoDeleteCall = stagesProductionApi.deleteById(stagesProductionUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(stagesProductionDtoDeleteCall, StagesProductionDto.class, id);
    }

    @Override
    public List<StagesProductionDto> search(String query) {
        List<StagesProductionDto> stagesProductionDtoList = new ArrayList<>();
        Call<List<StagesProductionDto>> stagesProductionDtoListCall = stagesProductionApi.search(stagesProductionUrl, query);
        try {
            stagesProductionDtoList = stagesProductionDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка StagesProductionDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка StagesProduction - ", e);
        }

        return stagesProductionDtoList;
    }

    @Override
    public List<StagesProductionDto> searchStagesProduction(Map<String, String> queryStagesProduction) {
        List<StagesProductionDto> stagesProductionDtoList = new ArrayList<>();
        Call<List<StagesProductionDto>> stagesProductionDtoListCall = stagesProductionApi.searchContractor(stagesProductionUrl, queryStagesProduction);
        try {
            stagesProductionDtoList = stagesProductionDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка StagesProductionDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка StagesProduction - ", e);
        }
        return stagesProductionDtoList;
    }
}

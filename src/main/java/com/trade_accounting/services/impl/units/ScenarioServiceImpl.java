package com.trade_accounting.services.impl.units;

import com.trade_accounting.models.dto.units.ScenarioDto;
import com.trade_accounting.services.api.units.ScenarioApi;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.units.ScenarioService;
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
public class ScenarioServiceImpl implements ScenarioService {

    private final ScenarioApi scenarioApi;

    private final String scenarioUrl;

    private final CallExecuteService<ScenarioDto> scenarioDtoCallExecuteService;

    public ScenarioServiceImpl(@Value("${scenario_url}") String scenarioUrl, Retrofit retrofit, CallExecuteService<ScenarioDto> scenarioDtoCallExecuteService) {
        this.scenarioUrl = scenarioUrl;
        scenarioApi = retrofit.create(ScenarioApi.class);
        this.scenarioDtoCallExecuteService = scenarioDtoCallExecuteService;
    }

    @Override
    public List<ScenarioDto> getAll() {
        Call<List<ScenarioDto>> scenarioDtoGetAll = scenarioApi.getAll(scenarioUrl);
        return scenarioDtoCallExecuteService.callExecuteBodyList(scenarioDtoGetAll, ScenarioDto.class);
    }

    @Override
    public List<ScenarioDto> search(Map<String, String> query) {
        List<ScenarioDto> scenarioDtoList = new ArrayList<>();
        Call<List<ScenarioDto>> scenarioDtoListCall = scenarioApi.search(scenarioUrl,query);

        try{
            scenarioDtoList = scenarioDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ScenarioDto");
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ScenarioDto - ", e);
        }
        return scenarioDtoList;
    }

    @Override
    public ScenarioDto getById(Long id) {
        Call<ScenarioDto> scenarioDtoCall = scenarioApi.getById(scenarioUrl, id);
        return scenarioDtoCallExecuteService.callExecuteBodyById(scenarioDtoCall, ScenarioDto.class, id);
    }

    @Override
    public void create(ScenarioDto scenarioDto) {
    Call<Void> scenarioDtoCall = scenarioApi.create(scenarioUrl,scenarioDto);
    scenarioDtoCallExecuteService.callExecuteBodyCreate(scenarioDtoCall,ScenarioDto.class);
    }

    @Override
    public void update(ScenarioDto scenarioDto) {
        Call<Void> scenarioDtoCall = scenarioApi.update(scenarioUrl,scenarioDto);
        scenarioDtoCallExecuteService.callExecuteBodyUpdate(scenarioDtoCall,ScenarioDto.class);
    }

    @Override
    public void deleteById(Long id) {
    Call<Void> scenarioDtoCall = scenarioApi.deleteById(scenarioUrl,id);
    scenarioDtoCallExecuteService.callExecuteBodyDelete(scenarioDtoCall,ScenarioDto.class,id);
    }

    @Override
    public List<ScenarioDto> findBySearch(String search) {
        List<ScenarioDto> scenarioDtoList = new ArrayList<>();
        Call<List<ScenarioDto>> scenarioDtoListCall = scenarioApi
                .searchByString(scenarioUrl, search.toLowerCase());

        try {
            scenarioDtoList = scenarioDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка сценариев");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка сценариев - ", e);
        }
        return scenarioDtoList;
    }
}

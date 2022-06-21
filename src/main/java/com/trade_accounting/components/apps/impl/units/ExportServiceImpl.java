package com.trade_accounting.services.impl.units;

import com.trade_accounting.models.dto.units.ExportDto;
import com.trade_accounting.services.interfaces.units.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportServiceImpl implements ExportService {

//    private final ExportApi exportApi;
//
//    private final String scenarioUrl;
//
//    private final CallExecuteService<ScenarioDto> scenarioDtoCallExecuteService;
//
//    public ExportServiceImpl(@Value("${scenario_url}") String scenarioUrl, Retrofit retrofit, CallExecuteService<ScenarioDto> scenarioDtoCallExecuteService) {
//        this.scenarioUrl = scenarioUrl;
//        scenarioApi = retrofit.create(ScenarioApi.class);
//        this.scenarioDtoCallExecuteService = scenarioDtoCallExecuteService;
//    }

    @Override
    public List<ExportDto> getAll() {
        return new ArrayList<>();
    }

    @Override
    public List<ExportDto> search(Map<String, String> query) {
        return null;
    }

    @Override
    public ExportDto getById(Long id) {
        return null;
    }

    @Override
    public void create(ExportDto scenarioDto) {

    }

    @Override
    public void update(ExportDto scenarioDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<ExportDto> findBySearch(String search) {
        return null;
    }
}
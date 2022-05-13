package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.ScenarioDto;

import java.util.List;
import java.util.Map;

public interface ScenarioService {

    List<ScenarioDto> getAll();

    List<ScenarioDto> search(Map<String, String> query);

    ScenarioDto getById(Long id);

    void create(ScenarioDto scenarioDto);

    void update(ScenarioDto scenarioDto);

    void deleteById(Long id);

    List<ScenarioDto> findBySearch(String search);
}

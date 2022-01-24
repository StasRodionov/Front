package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.StagesProductionDto;

import java.util.List;
import java.util.Map;

public interface StagesProductionService {

    List<StagesProductionDto> getALl();
    StagesProductionDto getById(long id);
    void create(StagesProductionDto dto);
    void update(StagesProductionDto dto);
    void deleteById(long id);
    List<StagesProductionDto> search(String query);
    List<StagesProductionDto> searchStagesProduction(Map<String, String> queryStagesProduction);

}

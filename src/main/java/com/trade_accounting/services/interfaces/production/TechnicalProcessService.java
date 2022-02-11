package com.trade_accounting.services.interfaces.production;

import com.trade_accounting.models.dto.production.TechnicalProcessDto;

import java.util.List;
import java.util.Map;

public interface TechnicalProcessService {

    List<TechnicalProcessDto> getAll();
    TechnicalProcessDto getById(long id);
    void create(TechnicalProcessDto dto);
    void update(TechnicalProcessDto dto);
    void deleteById(long id);
    List<TechnicalProcessDto> search(String query);
    List<TechnicalProcessDto> searchTechnicalProcess(Map<String, String> queryTechnicalProcess);

}

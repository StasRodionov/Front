package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.TechnicalProcessDto;

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

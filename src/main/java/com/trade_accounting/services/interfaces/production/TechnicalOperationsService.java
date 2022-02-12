package com.trade_accounting.services.interfaces.production;

import com.trade_accounting.models.dto.production.TechnicalOperationsDto;

import java.util.List;
import java.util.Map;

public interface TechnicalOperationsService {

    List<TechnicalOperationsDto> getAll();

    TechnicalOperationsDto getById(Long id);

    void create(TechnicalOperationsDto dto);

    void update(TechnicalOperationsDto dto);

    void deleteById(Long id);

    List<TechnicalOperationsDto> search(String query);

    List<TechnicalOperationsDto> searchTechnicalOperations(Map<String, String> queryTechnicalOperations);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

}

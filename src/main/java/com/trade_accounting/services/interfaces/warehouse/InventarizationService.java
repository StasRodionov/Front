package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.InventarizationDto;

import java.util.List;
import java.util.Map;

public interface InventarizationService {

    List<InventarizationDto> getAll();

    InventarizationDto getById(Long id);

    void create(InventarizationDto dto);

    void update(InventarizationDto dto);

    void deleteById(Long id);

    List<InventarizationDto> searchByFilter(Map<String, String> queryInventarization);

    List<InventarizationDto> searchByBetweenDataFilter(Map<String, String> queryInventarization);

    List<InventarizationDto> search(String search);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

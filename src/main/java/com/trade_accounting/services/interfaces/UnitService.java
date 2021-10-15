package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.UnitDto;

import java.util.List;
import java.util.Map;

public interface UnitService {

    List<UnitDto> getAll();

    UnitDto getById(Long id);

    void create(UnitDto unitDto);

    void update(UnitDto unitDto);

    void deleteById(Long id);

    List<UnitDto> search(Map<String, String> query);
}

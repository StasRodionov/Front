package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.ExportDto;

import java.util.List;
import java.util.Map;

public interface ExportService {

    List<ExportDto> getAll();

    List<ExportDto> search(Map<String, String> query);

    ExportDto getById(Long id);

    void create(ExportDto scenarioDto);

    void update(ExportDto scenarioDto);

    void deleteById(Long id);

    List<ExportDto> findBySearch(String search);
}

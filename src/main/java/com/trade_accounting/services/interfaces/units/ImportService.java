package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.ImportDto;

import java.util.List;
import java.util.Map;

public interface ImportService {

    List<ImportDto> getAll();
    ImportDto getById(Long id);
    void create(ImportDto importDto);
    void update(ImportDto importDto);
    void deleteById(Long id);
    List<ImportDto> search(Map<String, String> query);
    List<ImportDto> searchByString(String search);
}

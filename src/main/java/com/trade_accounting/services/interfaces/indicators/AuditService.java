package com.trade_accounting.services.interfaces.indicators;

import com.trade_accounting.models.dto.indicators.AuditDto;
import java.util.List;
import java.util.Map;


public interface AuditService {
    List<AuditDto> getAll();

    AuditDto getById(Long id);

    AuditDto create(AuditDto auditDto);

    void update(AuditDto AuditDto);

    void deleteById(Long id);

    List<AuditDto> quickSearch(String text);

    List<AuditDto> searchByFilter(Map<String, String> queryOperations);
}

package com.trade_accounting.services.interfaces.indicators;

import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.models.dto.warehouse.RemainDto;

import java.util.List;
import java.util.Map;


public interface AuditService {

    AuditDto create(AuditDto auditDto);

    void update(AuditDto auditDto);

    List<AuditDto> getAll();

    List<AuditDto> quickSearch(String text);

    List<AuditDto> searchByFilter(Map<String, String> queryOperations);
}

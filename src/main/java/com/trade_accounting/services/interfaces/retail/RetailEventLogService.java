package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.EventLogDto;

import java.util.List;
import java.util.Map;

public interface RetailEventLogService {

    List<EventLogDto> getAll();

    List<EventLogDto> search(Map<String, String> query);

    EventLogDto getById(Long id);

    void create(EventLogDto eventLogDto);

    void update(EventLogDto eventLogDto);

    void deleteById(Long id);

    List<EventLogDto> findBySearch(String search);
}

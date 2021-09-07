package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.AcceptanceDto;

import java.util.List;
import java.util.Map;

public interface AcceptanceService {

    List<AcceptanceDto> getAll();

    AcceptanceDto getById(Long id);

    AcceptanceDto create(AcceptanceDto acceptanceDto);

    void update(AcceptanceDto acceptanceDto);

    void deleteById(Long id);

    List<AcceptanceDto> searchByFilter(Map<String, String> queryAcceptance);

    List<AcceptanceDto> searchByString(String nameFilter);
}

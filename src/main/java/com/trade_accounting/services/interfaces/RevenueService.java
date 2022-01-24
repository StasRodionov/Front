package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.RevenueDto;

import java.util.List;
import java.util.Map;

public interface RevenueService {

    List<RevenueDto> getAll();

    RevenueDto getById(Long id);

    void create(RevenueDto revenueDto);

    void update(RevenueDto revenueDto);

    void deleteById(Long id);

    List<RevenueDto> search(Map<String, String> query);
}
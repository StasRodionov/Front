package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RevenueDto;

import java.util.List;

public interface RevenueService {

    List<RevenueDto> getAll();

    RevenueDto getById(Long id);

    void create(RevenueDto revenueDto);

    void update(RevenueDto revenueDto);

    void deleteById(Long id);
}
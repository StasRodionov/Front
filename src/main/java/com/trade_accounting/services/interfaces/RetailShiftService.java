package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailShiftDto;

import java.util.List;

public interface RetailShiftService {

    List<RetailShiftDto> getAll();

    RetailShiftDto getById(Long id);

    void create(RetailShiftDto retailShiftDto);

    void update(RetailShiftDto retailShiftDto);

    void deleteById(Long id);

}

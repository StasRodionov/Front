package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailShiftDto;

import java.util.List;
import java.util.Map;

public interface RetailShiftService {

    List<RetailShiftDto> getAll();

    RetailShiftDto getById(Long id);

    void create(RetailShiftDto retailShiftDto);

    void update(RetailShiftDto retailShiftDto);

    void deleteById(Long id);

    List<RetailShiftDto> search(String query);

    List<RetailShiftDto> searchByFilter(Map<String, String> query);

}

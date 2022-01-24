package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.AcceptanceProductionDto;
import com.trade_accounting.controllers.dto.RetailShiftDto;
import retrofit2.Response;

import java.util.List;

public interface AcceptanceProductionService {

    List<AcceptanceProductionDto> getAll();

    AcceptanceProductionDto getById(Long id);

    Response<AcceptanceProductionDto> create(AcceptanceProductionDto acceptanceProductionDto);

    void update(AcceptanceProductionDto acceptanceProductionDto);

    void deleteById(Long id);

    List<AcceptanceProductionDto> search(String query);
}

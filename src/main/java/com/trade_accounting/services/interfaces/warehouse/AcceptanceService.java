package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.AcceptanceDto;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public interface AcceptanceService {

    List<AcceptanceDto> getAll();

    AcceptanceDto getById(Long id);

    Response<AcceptanceDto> create(AcceptanceDto acceptanceDto);

    void update(AcceptanceDto acceptanceDto);

    void deleteById(Long id);

    List<AcceptanceDto> searchByFilter(Map<String, String> queryAcceptance);

    List<AcceptanceDto> search(String search);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);

}

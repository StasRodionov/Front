package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.MoneySubMutualSettlementsDto;

import java.util.List;
import java.util.Map;

public interface MoneySubMutualSettlementsService {

    List<MoneySubMutualSettlementsDto> getAll();

    void update(MoneySubMutualSettlementsDto MoneySubMutualSettlementsDto);

    List<MoneySubMutualSettlementsDto> searchByFilter(Map<String ,String> query);
}

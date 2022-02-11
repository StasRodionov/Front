package com.trade_accounting.services.interfaces.finance;

import com.trade_accounting.models.dto.finance.MoneySubMutualSettlementsDto;

import java.util.List;
import java.util.Map;

public interface MoneySubMutualSettlementsService {

    List<MoneySubMutualSettlementsDto> getAll();

    void update(MoneySubMutualSettlementsDto MoneySubMutualSettlementsDto);

    List<MoneySubMutualSettlementsDto> searchByFilter(Map<String ,String> query);
}

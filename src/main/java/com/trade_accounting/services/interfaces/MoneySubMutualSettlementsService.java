package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.MoneySubMutualSettlementsDto;

import java.util.List;
import java.util.Map;

public interface MoneySubMutualSettlementsService {

    List<MoneySubMutualSettlementsDto> getAll();

    void update(MoneySubMutualSettlementsDto MoneySubMutualSettlementsDto);

    List<MoneySubMutualSettlementsDto> filter(Map<String, String> query);
}

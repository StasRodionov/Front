package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.SerialNumbersDto;
import java.util.List;
import java.util.Map;

public interface SerialNumbersService {

    List<SerialNumbersDto> getAll();

    void update(SerialNumbersDto salesSubGoodsForSaleDto);

    List<SerialNumbersDto> searchByFilter(Map<String ,String> query);
}

package com.trade_accounting.services.interfaces.warehouse;


import com.trade_accounting.models.dto.warehouse.TypeOfPackingDto;

import java.util.List;

public interface TypeOfPackingService {

    List<TypeOfPackingDto> getAll();
}

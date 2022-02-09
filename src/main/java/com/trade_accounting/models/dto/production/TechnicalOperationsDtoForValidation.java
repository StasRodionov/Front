package com.trade_accounting.models.dto.production;

import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TechnicalOperationsDtoForValidation {

    private String idValid;

    private LocalDateTime dateValid;

    private TechnicalCardDto technicalCardDtoValid;

    private WarehouseDto warehouseDtoValid;

}

package com.trade_accounting.controllers.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductionTargetsDtoForValidation {

    private String idValid;

    private LocalDateTime dateValid;

    private CompanyDto companyDtoValid;

    private WarehouseDto materialWarehouseDtoValid;

    private WarehouseDto productionWarehouseDtoValid;

    private LocalDateTime plannedProductionDateValid;

    private LocalDateTime productionStartDateValid;

    private LocalDateTime productionEndDateValid;

}

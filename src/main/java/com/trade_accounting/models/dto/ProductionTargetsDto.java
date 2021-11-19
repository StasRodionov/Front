package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionTargetsDto extends ProductionTargetsDtoForValidation{

    private Long id;

    private String date;

    private Long companyId;

    private String deliveryPlannedMoment;

    private Long materialWarehouse;

    private Long productionWarehouse;

    private String productionStart;

    private String productionEnd;

    private Boolean shared = false;

    private String Owner;

    private String employeeOwner;

    private Boolean published = false;

    private Boolean printed = false;

    private String description;

    private String updated;

    private String updatedByName;

}

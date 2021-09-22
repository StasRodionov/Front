package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersOfProductionDto {

    private Long id;

    private String date;

    private Long companyId;

    private Long technicalCardId;

    private Integer volume;

    private Integer produce;

    private String PlannedProductionDate;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private String comment;
}

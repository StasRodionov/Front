package com.trade_accounting.models.dto.retail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetailCloudCheckDto {

    private Long id;

    private String date;

    private Long initiatorId;

    private Long fiscalizationPointId;

    private String status;

    private String cheskStatus;

    private Long total;

    private Long currencyId;

    private Long cashierId;
}

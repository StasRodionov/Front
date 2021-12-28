package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseControlDto {

    @NotNull
    private Long id;

    @NotNull
    private String productName;

    @NotNull
    private Long productCode;

    @NotNull
    private Long articleNumber;

    @NotNull
    private String productMeasure;


    private Long productQuantity;

    @NotNull
    private Long historyOfSalesId;

    @NotNull
    private Long currentBalanceId;

    @NotNull
    private Long forecastId;

}

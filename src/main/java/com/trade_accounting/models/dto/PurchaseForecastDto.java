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
public class PurchaseForecastDto {

    @NotNull
    private Long id;

    private Long reservedDays;

    private Long reservedProducts;

    private Boolean ordered;
}

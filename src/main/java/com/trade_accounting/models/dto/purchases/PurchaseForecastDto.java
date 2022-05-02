package com.trade_accounting.models.dto.purchases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseForecastDto {
    @NotNull
    private Long id;

    private Long reservedDays;

    private Long reservedProducts;

    private Long ordered;
}

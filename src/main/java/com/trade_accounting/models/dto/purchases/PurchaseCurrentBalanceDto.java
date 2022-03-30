package com.trade_accounting.models.dto.purchases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseCurrentBalanceDto {
    @NotNull
    private Long id;

    private String restOfTheWarehouse;

    private Long productsReserve;

    private Long productsAwaiting;

    private String productsAvailableForOrder;

    private Long daysStoreOnTheWarehouse;
}

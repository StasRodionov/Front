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
public class PurchaseCurrentBalanceDto {

    @NotNull
    private Long id;

    private Long restOfTheWarehouse;

    private Long productsReserve;

    private Long productsAwaiting;

    private Long productsAvailableForOrder;

    private Long daysStoreOnTheWarehouse;
}

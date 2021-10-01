package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDto {
    private Long id;

    private Long idProduct; //ProductDto

    private String description; //ProductDto

    private Long unitId; //ProductDto

    private int itemNumber; //ProductDto

    private BigDecimal amountAcceptance; //AcceptanceProductionDto

    private String incomingNumberDate; //AcceptanceDto

    private BigDecimal amountShipment; //InvoiceProductDto

}
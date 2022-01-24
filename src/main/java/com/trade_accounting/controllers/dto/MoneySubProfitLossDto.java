package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneySubProfitLossDto {
    private Long id;

    private String itemsList;

    private BigDecimal profitLoss;
}
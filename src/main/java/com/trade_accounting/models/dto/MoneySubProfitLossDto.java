package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneySubProfitLossDto {
    private BigDecimal revenue;

    private BigDecimal costPrice;

    private BigDecimal grossProfit;

    private BigDecimal operatingExpenses;

    private BigDecimal writeOffs;

    private BigDecimal rental;

    private BigDecimal salary;

    private BigDecimal marketing;

    private BigDecimal operatingProfit;

    private BigDecimal taxesAndFees;

    private BigDecimal netProfit;

}
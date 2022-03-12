package com.trade_accounting.models.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyArticleProfitLossDto {
    String article;

    BigDecimal profitLoss;
}

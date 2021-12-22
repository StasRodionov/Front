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
public class SalesSubGoodsForSaleDto {
    private String name;

    private Long code;

    private Long vendorCode;

    private Integer transferred;

    private Long amount;

    private BigDecimal sum;

    private Long returned;

    private BigDecimal remainder;
}

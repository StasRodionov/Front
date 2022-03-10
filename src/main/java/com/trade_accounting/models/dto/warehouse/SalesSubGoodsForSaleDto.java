package com.trade_accounting.models.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.stat.descriptive.summary.Product;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesSubGoodsForSaleDto {

    private Long id;

    private Long productId;

    private Long code;

    private Long vendorCode;

    private Integer transferred;

    private Integer accepted;

    private Long amount;

    private BigDecimal sum;

    private Long returned;

    private BigDecimal remainder;

    private Long reportAmount;

    private BigDecimal reportSum;

    private Long notReportAmount;

    private BigDecimal notReportSum;
}

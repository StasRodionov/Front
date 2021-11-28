package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnAmountByProductDto {
    private Long id;

    private Long productId;

    private Long invoiceId;

    private Long contractorId;

    private BigDecimal amount;
}

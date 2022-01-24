package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;

    private Long invoiceId;

    private Long productId;

    private BigDecimal amount;

    private BigDecimal price;


}

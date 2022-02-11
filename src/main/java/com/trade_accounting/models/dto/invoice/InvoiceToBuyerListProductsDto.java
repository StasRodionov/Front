/*
Счет покупателю, список товаров
 */
package com.trade_accounting.models.dto.invoice;

import com.trade_accounting.models.dto.warehouse.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceToBuyerListProductsDto {
    private Long id;

    private ProductDto productDto;

    private BigDecimal amount;

    private BigDecimal price;

    private BigDecimal sum;

    private String percentNds;

    private BigDecimal nds;

    private BigDecimal total;
}

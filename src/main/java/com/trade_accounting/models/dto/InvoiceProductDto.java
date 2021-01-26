package com.trade_accounting.models.dto;

import java.math.BigDecimal;

public class InvoiceProductDto {

    private Long id;

    private InvoiceDto invoiceDto;

    private ProductDto productDto;

    private BigDecimal amount;

    private BigDecimal price;

    public InvoiceProductDto(Long id,
                             InvoiceDto invoiceDto,
                             ProductDto productDto,
                             BigDecimal amount,
                             BigDecimal price) {
        this.id = id;
        this.invoiceDto = invoiceDto;
        this.productDto = productDto;
        this.amount = amount;
        this.price = price;
    }

}

package com.trade_accounting.models.dto;

import java.math.BigDecimal;

public class InvoiceProductDto {

    private Long id;

    private String invoiceDto; // todo: заменить String на InvoiceDto после реализации InvoiceDto

    private ProductDto productDto;

    private BigDecimal amount;

    private BigDecimal price;

    public InvoiceProductDto(Long id,
                             String invoiceDto, // todo: заменить String на InvoiceDto после реализации InvoiceDto
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

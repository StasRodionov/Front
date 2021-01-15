package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private LocalDateTime date;
    private TypeOfInvoiceDto typeOfInvoice;
    private CompanyDto company;
    private ContractorDto contractor;
    private boolean isSpend;
}

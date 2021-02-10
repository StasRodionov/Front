package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private String date;
    private String  typeOfInvoice;
    private CompanyDto company;
    private ContractorDto contractor;
    private WarehouseDto warehouseDto;
    private boolean isSpend;
}

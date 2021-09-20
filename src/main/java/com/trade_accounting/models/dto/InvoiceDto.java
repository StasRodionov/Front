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

    private String typeOfInvoice;

    private String companyId;

    private String contractorId;

    private String warehouseId;

    private CompanyDto companyDto;

    private ContractorDto contractorDto;

    private WarehouseDto warehouseDto;

    private boolean spend;

    private boolean isSpend;

    private String comment;
}

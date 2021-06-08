package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierAccountsDto {

    private Long id;

    private String date;

    private CompanyDto companyDto;

    private WarehouseDto warehouseDto;

    private ContractDto contractDto;

    private ContractorDto contractorDto;      // этого поля нет в back!

    private InvoiceProductDto invoiceProductDto;   // этого поля нет в back!

    private boolean isSpend;

    private String comment;
}

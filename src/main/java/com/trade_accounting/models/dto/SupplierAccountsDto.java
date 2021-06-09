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

    private ContractorDto contractorDto;

    private boolean isSpend;

    private String comment;
}

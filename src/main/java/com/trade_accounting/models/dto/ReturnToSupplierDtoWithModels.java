package com.trade_accounting.models.dto;

import lombok.Data;

@Data
public class ReturnToSupplierDtoWithModels {

    private CompanyDto companyDto;

    private WarehouseDto warehouseDto;

    private ContractorDto contractorDto;

    private ContractDto contractDto;

}

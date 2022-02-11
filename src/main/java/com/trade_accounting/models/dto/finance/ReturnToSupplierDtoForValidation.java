package com.trade_accounting.models.dto.finance;

import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReturnToSupplierDtoForValidation {

    private CompanyDto companyDtoValid;

    private WarehouseDto warehouseDtoValid;

    private ContractorDto contractorDtoValid;

    private ContractDto contractDtoValid;

    private String idValid;

    private LocalDateTime dateValid;

}

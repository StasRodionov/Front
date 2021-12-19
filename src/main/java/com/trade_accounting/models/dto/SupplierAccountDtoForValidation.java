package com.trade_accounting.models.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SupplierAccountDtoForValidation {

    private CompanyDto companyDtoValid;

    private WarehouseDto warehouseDtoValid;

    private ContractorDto contractorDtoValid;

    private ContractDto contractDtoValid;

    private String idValid;

    private LocalDateTime dateValid;

    private LocalDate paymentDateValid;
}

package com.trade_accounting.controllers.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BalanceAdjustmentDtoForValidation {

    private CompanyDto companyDtoValid;

    private ContractorDto contractorDtoValid;

    private String idValid;

    private LocalDateTime dateValid;
}

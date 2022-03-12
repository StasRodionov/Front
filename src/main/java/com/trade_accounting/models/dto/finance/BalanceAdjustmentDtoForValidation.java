package com.trade_accounting.models.dto.finance;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BalanceAdjustmentDtoForValidation {

    private CompanyDto companyDtoValid;

    private ContractorDto contractorDtoValid;

    private String idValid;

    private LocalDateTime dateValid;
}

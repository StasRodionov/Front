package com.trade_accounting.models.dto.company;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceListDtoForValidation {
    private LocalDateTime dateForValid;
    private CompanyDto companyDtoValid;
}

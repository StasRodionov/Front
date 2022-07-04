package com.trade_accounting.models.dto.company;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceListDtoForValidation {
    private LocalDateTime timeValid;
    private CompanyDto companyDtoValid;
}

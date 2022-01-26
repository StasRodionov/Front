package com.trade_accounting.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdersOfProductionDtoForValidation {

    private String idValid;

    private CompanyDto companyDtoValid;

    private TechnicalCardDto technicalCardDtoValid;

    private LocalDateTime dateValid;

    private LocalDateTime PlannedProductionDateValid;
}

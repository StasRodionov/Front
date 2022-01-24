package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailStoreDto {

    private Long id;

    private String name;

    private Boolean isActive;

    private String activityStatus;

    private BigDecimal revenue;

    private Long companyId;

    private CompanyDto companyDto;

    private String salesInvoicePrefix;

    private String defaultTaxationSystem;

    private String orderTaxationSystem;

    private List<EmployeeDto> cashiersDto;

    private List<Long> cashiersIds;
}

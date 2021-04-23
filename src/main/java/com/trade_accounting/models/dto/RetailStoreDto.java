package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailStoreDto {

    private Long id;
    private String name;
    private boolean isActive;
    private String activityStatus;
    private BigDecimal revenue;
    private String organization;
    private String salesInvoicePrefix;
    private String defaultTaxationSystem;
    private String orderTaxationSystem;

}

package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyersReturnDto extends ReturnToSupplierDtoForValidation {
    private Boolean isNew;

    private Long id;

    private String date;

    private Long companyId;

    private Long contractorId;

    private Long warehouseId;

    private BigDecimal sum;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;
}

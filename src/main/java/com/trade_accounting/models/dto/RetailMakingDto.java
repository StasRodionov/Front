package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailMakingDto {

    private Long id;

    private String date;

    private Long retailStoreId;

    private String fromWhom;

    private BigDecimal sum;

    private Long companyId;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;

//    private CompanyDto companyDto;
//
//    private RetailStoreDto retailStoreDto;
}

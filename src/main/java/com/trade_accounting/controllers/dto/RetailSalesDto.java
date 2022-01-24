package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailSalesDto {

    private Long id;

    private String time;

    private Long retailStoreId;

    private Long contractorId;

    private Long companyId;

    private BigDecimal sumCash;

    private BigDecimal sumNonСash;

    private BigDecimal prepayment;

    private BigDecimal sumDiscount;

    private BigDecimal sum;

    private Boolean sent;

    private  Boolean printed;

    private  String comment;

}

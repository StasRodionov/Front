package com.trade_accounting.models.dto.retail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailReturnsDto {

    private Long id;

    private String date;

    private Long retailStoreId;

    private BigDecimal cashAmount;

    private BigDecimal cashlessAmount;

    private BigDecimal totalAmount;

    private Boolean isSpend;

    private Boolean isSend;

    private Boolean isPrint;

    private String comment;

}

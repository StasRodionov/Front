package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneySubMutualSettlementsDto {

    private Long id;

    private Long contractorId;

    private Long employeeId;

    private String time;

    private Integer initialBalance;

    private Integer companyId;

    private Integer income;

    private Integer expenses;

    private Integer finalBalance;

    private BigDecimal bankcoming;

    private BigDecimal bankexpense;

    private BigDecimal bankbalance;

    private BigDecimal cashcoming;

    private BigDecimal cashexpense;

    private BigDecimal cashbalance;

    private BigDecimal allcoming;

    private BigDecimal allexpense;

    private BigDecimal allbalance;

}

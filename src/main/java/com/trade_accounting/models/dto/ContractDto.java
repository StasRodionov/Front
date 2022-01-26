package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    private Long id;

    private String number;

    private String contractDate;

    private LocalDate date;

    private Long companyId;

    private Long bankAccountId;

    private Long contractorId;

    private BigDecimal amount;

    private Boolean archive;

    private String comment;

    private Long legalDetailId;

    public void setDate(String contractDate) {
        this.date = LocalDate.parse(contractDate);
        this.contractDate = contractDate;
    }
}

package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuedInvoiceDto {

    private Long id;

    private String date;

    private Long companyId;

    private Long contractorId;

    private Boolean isSpend;

    private String comment;
}

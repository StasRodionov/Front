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

    private Long paymentId;

    private Boolean isSpend;

    private Boolean isSend;

    private Boolean isPrint;

    private String comment;
}

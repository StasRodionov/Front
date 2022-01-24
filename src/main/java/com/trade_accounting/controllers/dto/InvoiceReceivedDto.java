package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceReceivedDto {

    private Long id;

    private String data;

    private String incomData;

    private Long incomNumber;

    private Long companyId;

    private Long contractorId;

    private Long acceptanceId;

    private Boolean isSpend;

    private Boolean isSend;

    private Boolean isPrint;

    private String comment;
}

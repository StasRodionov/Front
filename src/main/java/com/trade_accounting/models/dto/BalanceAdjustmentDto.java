package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceAdjustmentDto {

    private Long id;

    private String date;

    private Long companyId;

    private Long contractorId;

    private String account;

    private String cashOffice;

    private String comment;

    private String dateChanged;

    private String whoChanged;
}

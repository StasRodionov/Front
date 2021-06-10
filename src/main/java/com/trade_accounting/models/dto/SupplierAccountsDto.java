package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierAccountsDto {

    private Long id;

    private String date;

    private Long nameCompany;

    private Long nameWarehouse;

    private Long numberContract;

    private Long nameContractor;

    private boolean isSpend;

    private String comment;
}

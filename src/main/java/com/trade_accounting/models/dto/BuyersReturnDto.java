package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyersReturnDto {

    private Long id;

    private String date;

    private CompanyDto companyDto;

    private ContractorDto contractorDto;

    private WarehouseDto warehouseDto;

    private Long sum;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;
}

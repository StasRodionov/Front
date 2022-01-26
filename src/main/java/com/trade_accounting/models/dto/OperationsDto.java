package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationsDto {

    private Long id;

    private Long companyId;

    private String comment;

    private String date;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private Long warehouseId;

    private Long warehouseToId;

    private Boolean isRecyclebin;
}

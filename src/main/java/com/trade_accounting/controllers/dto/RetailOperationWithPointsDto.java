package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailOperationWithPointsDto {
    private Long id;

    private Long number;

    private String currentTime;

    private String typeOperation;

    private Long numberOfPoints;

    private String accrualDate;

    private Long bonusProgramId;

    private Long contractorId;

    private Long taskId;

    private List<Long> fileIds;
}

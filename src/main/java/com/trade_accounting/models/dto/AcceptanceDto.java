package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceDto {

    private Long id;

    private String incomingNumber;

    private LocalDate incomingNumberDate;

    private Long contractorId;

    private Long projectId;

    private Long warehouseId;

    private Long contractId;

    private List<AcceptanceProductionDto> acceptanceProduction;
}

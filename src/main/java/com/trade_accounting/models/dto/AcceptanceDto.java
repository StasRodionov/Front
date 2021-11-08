package com.trade_accounting.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcceptanceDto extends AcceptanceDtoForValidation {

    private Long id;

    private String incomingNumber;

    private String incomingNumberDate;

    private Long contractorId;

    private Long warehouseId;

    private Long contractId;

    private String comment;

    private Boolean isSent = null;

    private Boolean isPrint = null;

    private List<AcceptanceProductionDto> acceptanceProduction;

    private List<Long>acceptanceProductIds;
}

package com.trade_accounting.models.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceDto extends AcceptanceDtoForValidation {

    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    private String incomingNumber;

    private String incomingNumberDate;

    private Long contractorId;

    private Long warehouseId;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate whenChangedDate;

    private Long companyId;

    private Long contractId;

    private Long employeeChangedId;

    private Long projectId;

    private String comment;

    private Boolean isSent;

    private Boolean isPrint;

    private Boolean isSpend;

    private List<AcceptanceProductionDto> acceptanceProduction;

    private List<Long> acceptanceProductIds;
}

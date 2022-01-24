package com.trade_accounting.controllers.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailPointsDto {

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
package com.trade_accounting.models.dto.indicators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuditDto {

    Long id;

    String description;

    String date;

    Long employeeId;

    String typeOfAudit;
}
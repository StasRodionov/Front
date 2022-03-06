package com.trade_accounting.models.dto.indicators;

import com.trade_accounting.models.dto.client.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuditDto {

    Long id;

    String description;

    LocalDateTime date;

    EmployeeDto employeeDto;
}
package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessParametersDto {

    private Long id;

    private Boolean generalAccess;

    private Long employeeId;

    private Long departmentId;
}
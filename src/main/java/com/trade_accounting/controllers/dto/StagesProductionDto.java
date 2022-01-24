package com.trade_accounting.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StagesProductionDto {

    private Long id;

    private String name;

    private String description;

    private Long departmentId;

    private Long employeeId;

    private DepartmentDto departmentDto;

    private EmployeeDto employeeDto;

}

// Этапы
package com.trade_accounting.models.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductGroupDto {

    private Long id;

    private String name;

    private String sortNumber;

    private Boolean serviceGroup = false;

    private Long parentId;

    private String description;

    private String saleTax;

    private Long taxSystemId;

    private Long employeeId;

    private Long departmentId;
}

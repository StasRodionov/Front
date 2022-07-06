package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesChannelDto {

    private Long id;

    private String name;

    private String type;

    private String description;

    private boolean generalAccess;

    private String departmentOwner;

    private String employeeOwner;

    private String dateOfChange;

    private String employeeChange;

    public SalesChannelDto(String name, String type, String description, boolean generalAccess, String departmentOwner, String employeeOwner, String dateOfChange, String employeeChange) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.generalAccess = generalAccess;
        this.departmentOwner = departmentOwner;
        this.employeeOwner = employeeOwner;
        this.dateOfChange = dateOfChange;
        this.employeeChange = employeeChange;
    }
}

package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {
    private Long id;

    private String name;

    private String sortNumber;

    private String address;

    private String commentToAddress;

    private String comment;

}

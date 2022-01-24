package com.trade_accounting.controllers.dto;

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
}

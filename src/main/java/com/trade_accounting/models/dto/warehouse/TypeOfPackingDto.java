package com.trade_accounting.models.dto.warehouse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfPackingDto {

    private Long id;

    private String name;

    private String sortNumber;
}

package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfPriceDto {

    private Long id;

    private String name;

    private String sortNumber;

    public TypeOfPriceDto(String name, String sortNumber) {
        this.name = name;
        this.sortNumber = sortNumber;
    }
}

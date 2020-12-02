package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDto {

    private Long id;

    private String shortName;

    private String fullName;

    private String sortNumber;

    public UnitDto(String shortName, String fullName, String sortNumber){
        this.shortName = shortName;
        this.fullName = fullName;
        this.shortName = sortNumber;
    }

}

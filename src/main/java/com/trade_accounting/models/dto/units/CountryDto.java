package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {
    private Long id;
    private String type;
    private String shortName;
    private String fullName;
    private String digitCode;
    private String twoLetterCode;
    private String threeLetterCode;
}

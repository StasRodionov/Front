package com.trade_accounting.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    private Long id;
    private TypeOfPriceDto typeOfPriceDto;
    private BigDecimal value;
}

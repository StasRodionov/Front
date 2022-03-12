package com.trade_accounting.models.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceDto {
    private Long id;
    private Long typeOfPriceId;
    private BigDecimal value;
}

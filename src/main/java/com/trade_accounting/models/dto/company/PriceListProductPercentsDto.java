package com.trade_accounting.models.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListProductPercentsDto extends PriceListProductPercentsDtoForValidation {
    private Long id;
    private String name;
    private BigDecimal percent;
    private Long priceListId;
}

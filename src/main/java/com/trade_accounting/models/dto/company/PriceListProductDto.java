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
public class PriceListProductDto {
    private Long id;
    private Long priceListId;
    private Long productId;
    private BigDecimal price;
}

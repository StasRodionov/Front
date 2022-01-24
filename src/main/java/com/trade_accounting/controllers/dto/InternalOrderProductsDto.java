package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalOrderProductsDto extends InternalOrderProductsDtoForValidation {
    private Long id;

    private Long productId;

    private BigDecimal amount;

    private BigDecimal price;
}

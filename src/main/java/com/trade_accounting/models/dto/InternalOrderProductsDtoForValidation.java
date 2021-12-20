package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalOrderProductsDtoForValidation {
    private Long idValid;

    private Long productIdValid;

    private BigDecimal amountValid;

    private BigDecimal priceValid;
}

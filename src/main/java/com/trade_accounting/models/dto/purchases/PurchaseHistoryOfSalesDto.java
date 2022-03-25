package com.trade_accounting.models.dto.purchases;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseHistoryOfSalesDto {
    @NotNull
    private Long id;

    private BigDecimal sumOfProducts;

    private Long productPriceId;

    private BigDecimal productMargin;

    private BigDecimal productProfitMargin;

    private Long productSalesPerDay;
}

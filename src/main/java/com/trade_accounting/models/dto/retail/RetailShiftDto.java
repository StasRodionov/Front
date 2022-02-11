package com.trade_accounting.models.dto.retail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailShiftDto {

    private Long id;

    private String dataOpen;

    private String dataClose;

    private Long retailStoreId;

    private Long warehouseId;

    private Long companyId;

    private String bank;

    private BigDecimal revenuePerShift;

    private BigDecimal received;

    private BigDecimal amountOfDiscounts;

    private BigDecimal commission_amount;

    private Boolean sent;

    private Boolean printed;

    private String comment;
}

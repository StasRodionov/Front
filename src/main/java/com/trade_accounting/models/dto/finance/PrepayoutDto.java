package com.trade_accounting.models.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrepayoutDto {

    private Long id;

    @NotNull
    private String date;

    @NotNull
    private Long retailStoreId;

    @NotNull
    private Long contractorId;

    @NotNull
    private Long companyId;

    private BigDecimal cash;

    private BigDecimal cashless;

    private BigDecimal discount;

    private BigDecimal sum;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;

}


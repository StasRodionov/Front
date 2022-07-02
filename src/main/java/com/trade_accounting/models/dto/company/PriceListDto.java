package com.trade_accounting.models.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDto extends PriceListDtoForValidation{

    private Long id;

    @NotNull
    private String number;

    private String date;

    @NotNull
    private Long companyId;

    private Boolean isSent;

    private Boolean isPrint;

    private String comment;

    private List<Long> productsIds;

    private Boolean isSpend;

    private Boolean isRecyclebin;

    private Long typeOfPriceId;

    private List<Long> percentsIds;
}

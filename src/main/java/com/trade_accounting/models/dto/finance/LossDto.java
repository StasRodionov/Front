package com.trade_accounting.models.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossDto extends LossDtoForValidation {

    private Long id;

    private String date;

    private Long warehouseId;

    private Long companyId;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private String comment;

    private List<Long> lossProductsIds;

    private Boolean isRecyclebin;

    public void setInternalOrderProductsIds(List<Long> idList) {
    }
}

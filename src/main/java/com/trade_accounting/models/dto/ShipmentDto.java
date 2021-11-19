package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDto {

    private Long id;

    private String date;

    private Long warehouseId;

    private Long companyId;

    private Long contractorId;

    private List<Long> shipmentProductsIds;

    private BigDecimal sum;

    private BigDecimal paid;

    private Boolean isSpend;

    private Boolean isSend;

    private Boolean isPrint;

    private String comment;
}

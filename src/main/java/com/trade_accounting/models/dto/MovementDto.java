package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {
    private Long id;

    private String date;

    private String whenChangedDate;

    private Long warehouseFromId;

    private BigDecimal sum;

    private Long warehouseToId;

    private Long companyId;

    private Long employeeChangedId;

    private Long projectId;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private Boolean isSpend = false;

    private String comment;

    private List<Long> movementProductsIds;
}

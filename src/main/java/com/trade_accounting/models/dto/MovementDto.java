package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {
    private Long id;

    private String date;

    private Long warehouseFromId;

    private Long warehouseToId;

    private Long companyId;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private String comment;

    private List<Long> movementProductsIds;
}

package com.trade_accounting.models.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerialNumbersDto {

    private Long id;

    private Long code;

    private Long vendorCode;

    private Long productId;

    private Long warehouseId;

    private String typeDocument;

    private Long documentNumber;

    private String description;
}

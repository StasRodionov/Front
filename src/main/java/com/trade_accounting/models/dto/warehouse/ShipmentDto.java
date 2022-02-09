package com.trade_accounting.models.dto.warehouse;

import com.trade_accounting.models.dto.finance.ReturnToSupplierDtoForValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDto  extends ReturnToSupplierDtoForValidation {

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

    private Boolean isRecyclebin;
}

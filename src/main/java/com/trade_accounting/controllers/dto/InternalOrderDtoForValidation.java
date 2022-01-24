package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Andrusov
 * @version 1.0.0
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalOrderDtoForValidation {

    private List<Long> internalOrderProductsIdsValid;

    private LocalDateTime dateValid;

    private CompanyDto companyDtoValid;

    private WarehouseDto warehouseDtoValid;

    private String idValid;
}

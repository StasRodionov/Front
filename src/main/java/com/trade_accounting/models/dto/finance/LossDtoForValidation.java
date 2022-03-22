package com.trade_accounting.models.dto.finance;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LossDtoForValidation {

    private LocalDateTime dateValid;

    private WarehouseDto warehouseDtoValid;

    private CompanyDto companyDtoValid;

    private List<Long> lossProductsIdsValid;

}

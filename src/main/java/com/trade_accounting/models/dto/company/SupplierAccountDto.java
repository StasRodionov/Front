package com.trade_accounting.models.dto.company;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.company.SupplierAccountDtoForValidation;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierAccountDto extends SupplierAccountDtoForValidation {

    private Long id;

    private String date;

    private Long companyId;

    private Long warehouseId;

    private Long contractId;

    private Long contractorId;

    private Boolean isSpend;

    private String comment;

    @EqualsAndHashCode.Exclude
    private WarehouseDto warehouseDto;

    @EqualsAndHashCode.Exclude
    private ContractorDto contractorDto;

    @EqualsAndHashCode.Exclude
    private CompanyDto companyDto;

    @EqualsAndHashCode.Exclude
    private ContractDto contractDto;

    private String typeOfInvoice;

    private String plannedDatePayment;

    private Boolean isRecyclebin;

}

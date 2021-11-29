package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
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

}

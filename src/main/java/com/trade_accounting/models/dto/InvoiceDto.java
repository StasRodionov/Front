package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;

    private String date;

    private String typeOfInvoice;

    private Long companyId;

    private Long contractorId;

    private Long warehouseId;

    private Boolean isSpend;

    private String comment;

    private Long invoicesStatusId;

    @Override
    public String toString() {
        return  "Заказ № = " + id +
                ", Дата ='" + date + '\'' +
                ", typeOfInvoice ='" + typeOfInvoice + '\'' +
                ", Организация = " + companyId +
                ", Контрагент = " + contractorId +
                ", Склад = " + warehouseId +
                ", Проведено = " + isSpend +
                ", Комментарий = '" + comment + '\'';
    }

    @EqualsAndHashCode.Exclude
    private WarehouseDto warehouseDto;

    @EqualsAndHashCode.Exclude
    private ContractorDto contractorDto;

    @EqualsAndHashCode.Exclude
    private CompanyDto companyDto;
}

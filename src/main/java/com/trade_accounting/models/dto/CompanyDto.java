package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;

    private String name;

    private String inn;

    private String sortNumber;

    private String phone;

    private String fax;

    private String email;

    private Boolean payerVat;

   // private String address;

    @EqualsAndHashCode
    private AddressDto addressDto; //внести изменения в классы, использующие CompanyDto


    private String commentToAddress;

    private String leader;

    private String leaderManagerPosition;

    private String leaderSignature;

    private String chiefAccountant;

    private String chiefAccountantSignature;

    private String stamp;

    @EqualsAndHashCode.Exclude
    private LegalDetailDto legalDetailDto;

    @EqualsAndHashCode.Exclude
    private List<BankAccountDto> bankAccountDto;
}

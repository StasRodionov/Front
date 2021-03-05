package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractorDto {

    private Long id;
    private String name;
    private String inn;
    private String sortNumber;
    private String phone;
    private String fax;
    private String email;
    private String address;
    private String commentToAddress;
    private String comment;
    private ContractorGroupDto contractorGroupDto;
    private TypeOfContractorDto typeOfContractorDto;
    private TypeOfPriceDto typeOfPriceDto;
    private List<BankAccountDto> bankAccountDto;
    private LegalDetailDto legalDetailDto;
}

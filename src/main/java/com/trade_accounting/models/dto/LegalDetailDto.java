package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalDetailDto {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    private String address;

    private String commentToAddress;

    private String inn;

    private String okpo;

    private String ogrnip;

    private String numberOfTheCertificate;

    private String  dateOfTheCertificate;

    private LocalDate date;

    private TypeOfContractorDto typeOfContractorDto;

    public void setDate(String dateOfTheCertificate) {
        this.date = LocalDate.parse(dateOfTheCertificate);
    }

}


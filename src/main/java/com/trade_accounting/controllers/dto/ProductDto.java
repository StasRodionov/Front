package com.trade_accounting.controllers.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal weight;

    private BigDecimal volume;

    private BigDecimal purchasePrice;

    private String description;

    private Long unitId;

    private Boolean archive = false;

    private Boolean service = false;

    private Long contractorId;

    private List<Long> productPriceIds;

    private Long taxSystemId;

    private List<ImageDto> imageDtos;

    private List<FileDto> fileDtos;

    private Long productGroupId;

    private Long attributeOfCalculationObjectId;

    private String countryOrigin;

    private int itemNumber;

    private String saleTax;

    private int minimumBalance;



}

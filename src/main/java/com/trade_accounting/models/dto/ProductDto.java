package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    private List<ProductPriceDto> productPriceDtos;

    private Long taxSystemId;

    private List<ImageDto> imageDtos;

    private Long productGroupId;

    private Long attributeOfCalculationObjectId;

    private String countryOrigin;

    private int itemNumber;

    private String saleTax;

    private int minimumBalance;

}

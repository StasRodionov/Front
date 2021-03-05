package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal weight;

    private BigDecimal volume;

    private BigDecimal purchasePrice;

    private String description;

    private UnitDto unitDto;

    private Boolean archive = false;

    private ContractorDto contractorDto;

    private List<PriceDto> priceDtos;

    private TaxSystemDto taxSystemDto;

    private List<ImageDto> imageDto;

    private ProductGroupDto productGroupDto;

    private AttributeOfCalculationObjectDto attributeOfCalculationObjectDto;

}

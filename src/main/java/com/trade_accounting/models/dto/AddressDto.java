package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String index;
    private String country;
    private String region;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String another;

    public String getAnother() {
        return index +
                ", " + country +
                ", " + region +
                ", " + city +
                ", " + street +
                ", д." + house +
                ", кв." + apartment;
    }

    @Override
    public String toString() {
        return index +
                ", " + country +
                ", " + region +
                ", " + city +
                ", " + street +
                ", " + house +
                ", " + apartment +
                ", " + another;
    }
}

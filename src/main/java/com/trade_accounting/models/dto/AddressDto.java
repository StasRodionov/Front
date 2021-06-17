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

    private String[] getArrayAddress() {
        return new String[]{index, country, region, city, street, "д." + house, "кв." + apartment};
    }

    public String getAnother() {
        StringBuilder result = new StringBuilder();
        final String[] arrayAddress = getArrayAddress();
        for (int i = 0; i < arrayAddress.length; i++) {
            String s = arrayAddress[i];
            if (s != null && !s.equals("") && !s.equals("д.") && !s.equals("кв.")) {
                result.append(arrayAddress[i]);
                if (i != arrayAddress.length - 1) {
                    result.append(", ");
                }
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return getAnother();
    }
}

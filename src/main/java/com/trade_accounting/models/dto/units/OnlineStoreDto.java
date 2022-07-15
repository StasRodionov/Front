package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineStoreDto {
    private Long id;
    private String name;
    private String type;
    private String orders;
    private String remains;
}

package com.trade_accounting.models.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnsMaskDto {

    private int gridDtoId;

    private int employeeDtoId;

    private int mask;

    public ColumnsMaskDto(int gridDtoId) {
        this(gridDtoId, Integer.MAX_VALUE);
    }

    public ColumnsMaskDto(int gridDtoId, int mask) {
        this.gridDtoId = gridDtoId;
        this.mask = mask;
    }
}

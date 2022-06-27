package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDto {
    private Long id;
    private String task;
    private String start;
    private String end;
    private String status;
}

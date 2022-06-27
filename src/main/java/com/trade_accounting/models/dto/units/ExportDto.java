package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportDto {

    private Long id;

    private String task;

    private String start;

    private String finish;

    private String status;

}

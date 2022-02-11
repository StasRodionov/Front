package com.trade_accounting.models.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicesStatusDto {

    private Long id;

    private String statusName;
}

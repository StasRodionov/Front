package com.trade_accounting.models.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDto {

    private Long id;

    @NotNull
    private String number;

    private String time;

    @NotNull
    private Long companyId;

    private Boolean sent;

    private Boolean printed;

    private String commentary;
}

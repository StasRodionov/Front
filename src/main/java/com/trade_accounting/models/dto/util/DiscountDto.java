package com.trade_accounting.models.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto implements Serializable {
    Long id;
    Boolean isActive;
    @NotNull
    String name;
    @NotNull
    String type;
    Boolean isBonusProgram;
    Long bonusProgramId;
}

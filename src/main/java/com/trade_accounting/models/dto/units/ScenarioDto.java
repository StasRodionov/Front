package com.trade_accounting.models.dto.units;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioDto {

     Long id;

     String name;

     String comment;

     Boolean activeStatus;
}

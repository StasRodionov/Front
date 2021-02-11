package com.trade_accounting.models.dto;

import com.vaadin.flow.component.html.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductGroupDto {

    private Long id;

    private String name;

    private String sortNumber;

    private Long parentId;
}

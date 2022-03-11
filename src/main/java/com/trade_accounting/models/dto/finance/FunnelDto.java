package com.trade_accounting.models.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunnelDto {

    private Long id;

    private String statusName;

    private Long count;

    private String time;

    private String conversion;

    private String price;

    //Конструктор для listOrdersDataView
    public FunnelDto(String statusName, Long count, String time, String conversion, String price) {
        this.statusName = statusName;
        this.count = count;
        this.time = time;
        this.conversion = conversion;
        this.price = price;
    }

    //Конструктор для listContractorsDataView
    public FunnelDto(String statusName, Long count, String conversion) {
        this.statusName = statusName;
        this.count = count;
        this.conversion = conversion;
    }
}

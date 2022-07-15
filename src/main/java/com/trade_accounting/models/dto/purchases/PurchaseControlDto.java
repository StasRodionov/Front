package com.trade_accounting.models.dto.purchases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseControlDto {

    @NotNull
    private Long id;

    @NotNull
    private Long productNameId;

    @NotNull
    private String date;

    @NotNull
    private Long productCode;

    @NotNull
    private Long articleNumber;

    @NotNull
    private String productMeasure;

    @NotNull
    private Long companyId;

    @NotNull
    private Long warehouseId;

    @NotNull
    private Long contractorId;

    @NotNull
    private Long salesChannelId;

    @NotNull
    private Long productQuantity;

    @NotNull
    private Long historyOfSalesId;

    @NotNull
    private Long currentBalanceId;

    @NotNull
    private Long forecastId;

//    private String buttonSetting;   /*Заглушка для кнопки настроек грида*/

}

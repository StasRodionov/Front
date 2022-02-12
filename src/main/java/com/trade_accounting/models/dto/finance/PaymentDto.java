package com.trade_accounting.models.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;

    private String typeOfPayment;

    private String typeOfDocument;

    private String paymentMethods;

    private String expenseItem;

    @NotNull
    private String number;

    private String time;

    private String date;

    @NotNull
    private Long companyId;

    @NotNull
    private Long contractorId;

    private Long contractId;

    private Long projectId;

    private BigDecimal sum;

    private Boolean isConducted;

    private Boolean isRecyclebin;
}

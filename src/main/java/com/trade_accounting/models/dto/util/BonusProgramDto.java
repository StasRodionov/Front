package com.trade_accounting.models.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusProgramDto {

    Long id;

    String name;

    Boolean activeStatus;

    Boolean allContractors;

    List<Long> contractorGroupIds;

    BigDecimal accrualRule;

    BigDecimal writeOffRules;

    Short maxPaymentPercentage;

    Short numberOfDays;

    Boolean welcomePoints;

    Long numberOfPoints;

    Boolean registrationInBonusProgram;

    Boolean firstPurchase;
}

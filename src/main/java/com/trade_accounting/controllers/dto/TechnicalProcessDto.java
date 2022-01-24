package com.trade_accounting.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Класс-dto Тех.процесса (вкладка производство)
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TechnicalProcessDto {

    private Long id;

    /**
     * имя техпроцесса
     */
    @NotNull
    private String name;

    /**
     * описание техпроцесса
     */
    private String description;

    /**
     * этапы производства, что относятся к техпроцессу, ссылки через ID
     */
    private Set<Long> stagesProductionIds;

    /**
     * техпроцесс в архиве или действующий
     */
    private Boolean isArchived = false;

    /**
     * доступ к техпроцессу - по умолчанию "В общем доступе" - "true"
     */
    private Boolean isShared = true;

    /**
     * владелец-отдел данного техпроцесса - ссылка на его ID
     */
    @NotNull
    private Long departmentOwnerId;

    /**
     * владелец-сотрудник данного техпроцесса - ссылка на его ID
     */
    @NotNull
    private Long employeeOwnerId;

    /**
     * дата последнего изменения техпроцесса - строкой
     */
    @NotNull
    private String dateOfChanged;

    /**
     * сотрудник, внёсший изменения в техпроцесс последним - ссылка на его ID
     */
    @NotNull
    private Long employeeWhoLastChangedId;

//    private DepartmentDto departmentDto;
//    private EmployeeDto employeeDto;


}

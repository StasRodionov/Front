package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;

    private String description;

    private Long employeeId; //EmployeeDto

    private Long taskAuthorId; //EmployeeDto

    private String creationDateTime;

    private String deadlineDateTime;

    private boolean completed;

    //private int commentCount;
    private List<Long> taskCommentsIds;
}

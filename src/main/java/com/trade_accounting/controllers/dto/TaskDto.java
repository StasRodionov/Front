package com.trade_accounting.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;

    private String description;

    private Long contractorId;

    private Long employeeId; //EmployeeDto

    private Long taskAuthorId; //EmployeeDto

    private String creationDateTime;

    private String deadlineDateTime;

    private boolean completed;

    //private int commentCount;
    private List<Long> taskCommentsIds;

    public String getCreationDataTimeNotNull(){
        return creationDateTime == null ? String.valueOf(LocalDateTime.now()) : creationDateTime;
    }

    public String getDeadLineDataTimeNotNullPlus60min(){
        return deadlineDateTime == null ? String.valueOf(LocalDateTime.now().plusMinutes(60)) : deadlineDateTime;
    }
}

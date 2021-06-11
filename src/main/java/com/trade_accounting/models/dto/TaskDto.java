package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private long id;

    private String description;

    private Long employeeId;

    private Long taskAuthorId;

    private LocalDateTime creationDateTime;

    private LocalDateTime deadlineDateTime;

    private boolean completed;

    private int commentCount;
}

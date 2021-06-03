package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentDto {

    private Long id;

    private String commentContent;

    private Long publisherId;

    private LocalDateTime publishedDateTime;

    private Long taskId;
}

package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.TaskCommentDto;

import java.util.List;

public interface TaskCommentService {

    List<TaskCommentDto> getAll();

    TaskCommentDto getById(Long id);

    void create(TaskCommentDto taskCommentDto);

    void update(TaskCommentDto taskCommentDto);

    void deleteById(Long id);
}

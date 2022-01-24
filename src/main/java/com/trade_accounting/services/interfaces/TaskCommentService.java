package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.TaskCommentDto;

import java.util.List;

public interface TaskCommentService {

    List<TaskCommentDto> getAll();

    TaskCommentDto getById(Long id);

    void create(TaskCommentDto taskCommentDto);

    void update(TaskCommentDto taskCommentDto);

    void deleteById(Long id);
}

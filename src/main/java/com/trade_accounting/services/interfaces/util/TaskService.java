package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.TaskDto;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskDto> getAll();

    List<TaskDto> searchBy(String searchTask);

    List<TaskDto> searchByFilter(Map<String, String> query);

    TaskDto getById(Long id);

    void create(TaskDto taskDto);

    void update(TaskDto taskDto);

    void deleteById(Long id);


}
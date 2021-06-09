package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.TaskService;
import com.trade_accounting.services.interfaces.api.TaskApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskApi taskApi;
    private final String taskUrl;


    @Autowired
    public TaskServiceImpl(@Value("${task_url}") String taskUrl, Retrofit retrofit) {
        taskApi = retrofit.create(TaskApi.class);
        this.taskUrl = taskUrl;
    }

    @Override
    public List<TaskDto> getAll() {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.getAll(taskUrl);
        try {
            taskDtoList = taskDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TaskDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка TaskDto: {}", e);
        }
        return taskDtoList;
    }

    @Override
    public List<TaskDto> searchBy(String searchTask) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.searchBy(taskUrl, searchTask);
        try {
            taskDtoList = taskDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка по условию: {} из TaskDto", searchTask);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка по условию: {} из TaskDto: {}", searchTask, e);
        }
        return taskDtoList;
    }

    @Override
    public List<TaskDto> searchByFilter(Map<String, String> query) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.searchByFilter(taskUrl, query);
        try {
            taskDtoList = taskDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка по условию: {} из TaskDto", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка по условию: {} из TaskDto: {}", query, e);
        }
        return taskDtoList;
    }

    @Override
    public TaskDto getById(Long id) {
        TaskDto taskDto = new TaskDto();
        Call<TaskDto> taskDtoCall = taskApi.getById(taskUrl, id);
        try {
            taskDto = taskDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра TaskDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение TaskDto с id = {}: {}", id, e);
        }
        return taskDto;
    }

    @Override
    public void create(TaskDto taskDto) {
        Call<Void> taskDtoCall = taskApi.create(taskUrl, taskDto);

        try {
            taskDtoCall.execute();
            log.info("Успешно выполнен запрос на создание нового экземпляра {}", taskDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}", taskDto, e);
        }
    }

    @Override
    public void update(TaskDto taskDto) {
        Call<Void> taskDtoCall = taskApi.update(taskUrl, taskDto);

        try {
            taskDtoCall.execute();
            log.info("Успешно выполнен запрос на изменение экземпляра {}", taskDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на изменение экземпляра {}: {}", taskDto, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taskDtoCall = taskApi.deleteById(taskUrl, id);

        try {
            taskDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление экземпляра с id {}: {}", id, e);
        }
    }
}
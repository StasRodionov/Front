package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaskCommentDto;
import com.trade_accounting.services.interfaces.TaskCommentService;
import com.trade_accounting.services.interfaces.api.TaskCommentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskCommentServiceImpl implements TaskCommentService {

    private final TaskCommentApi taskCommentApi;
    private final String taskCommentUrl;

    @Autowired
    public TaskCommentServiceImpl(@Value("${task_comment_url}") String taskCommentUrl, Retrofit retrofit) {
        taskCommentApi = retrofit.create(TaskCommentApi.class);
        this.taskCommentUrl = taskCommentUrl;
    }

    @Override
    public List<TaskCommentDto> getAll() {
        List<TaskCommentDto> taskCommentDtoList = new ArrayList<>();
        Call<List<TaskCommentDto>> taskCommentDtoListCall = taskCommentApi.getAll(taskCommentUrl);
        try {
            taskCommentDtoList = taskCommentDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка TaskCommentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка TaskCommentDto: {}", e);
        }
        return taskCommentDtoList;

    }

    @Override
    public void create(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.create(taskCommentUrl, taskCommentDto);

        try {
            taskCommentDtoCall.execute();
            log.info("Успешно выполнен запрос на создание нового экземпляра {}", taskCommentDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на создание нового экземпляра {}: {}", taskCommentDto, e);
        }
    }

    @Override
    public void update(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.update(taskCommentUrl, taskCommentDto);

        try {
            taskCommentDtoCall.execute();
            log.info("Успешно выполнен запрос на изменение экземпляра {}", taskCommentDto);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на изменение экземпляра {}: {}", taskCommentDto, e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taskCommentDtoCall = taskCommentApi.deleteById(taskCommentUrl, id);

        try {
            taskCommentDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление экземпляра с id {}: {}", id, e);
        }
    }
}
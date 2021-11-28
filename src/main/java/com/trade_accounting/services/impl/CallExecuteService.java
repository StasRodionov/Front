package com.trade_accounting.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CallExecuteService<T> {
    private T object;

    public List<T> callExecuteBodyList(Call<List<T>> call, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        try {
            list.addAll(Objects.requireNonNull(call.execute().body()));
            log.info("Успешно выполнен запрос на получение списка " + tClass.getSimpleName());
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка " + tClass.getSimpleName() + e);
        }
        return list;
    }

    /**
     * @param call
     * @param tClass класс дя г
     * @param id
     * @return
     */
    public T callExecuteBodyById(Call<T> call, Class<T> tClass, Long id) {
        try {
            object = call.execute().body();
            log.info(String.format("Успешно выполнен запрос на получение %s по id %d", tClass.getSimpleName(), id));
        } catch (IOException e) {
            log.error(String.format("Произошла ошибка при отправке запроса на получение %s по id %d", tClass.getSimpleName(), id));
        }
        return object;
    }

    public void callExecuteBodyCreate(Call<Void> call, Class<T> tClass) {

        try {
            call.execute();
            log.info(String.format("Успешно выполнен запрос на создание экземпляра %s", tClass.getSimpleName()));
        } catch (IOException e) {
            log.error(String.format("Произошла ошибка при выполнении запроса на создание экземпляра %s", tClass.getSimpleName()));
        }
    }

    public void callExecuteBodyUpdate(Call<Void> call, Class<T> tClass) {
        try {
            call.execute();
            log.info(String.format("Успешно выполнен запрос на обновление экземпляра %s", tClass.getSimpleName()));
        } catch (IOException e) {
            log.error(String.format("Произошла ошибка при выполнении запроса на обновление экземпляра %s", tClass.getSimpleName()));
        }
    }

    public void callExecuteBodyDelete(Call<Void> call, Class<T> tClass, Long id) {
        try {
            call.execute();
            log.info(String.format("Успешно выполнен запрос на удаление экземпляра %s c id = %d", tClass.getSimpleName(), id));
        } catch (IOException e) {
            log.error(String.format("Произошла ошибка при выполнении запроса на обновление экземпляра %s c id = %d", tClass.getSimpleName(), id));
        }
    }

}

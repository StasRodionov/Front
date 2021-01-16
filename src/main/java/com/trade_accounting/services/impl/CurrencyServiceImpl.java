package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CurrencyDto;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.api.CurrencyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyApi currencyApi;
    private final String currencyUrl;

    public CurrencyServiceImpl(@Value("${currency_url}") String currencyUrl, Retrofit retrofit) {
        this.currencyUrl = currencyUrl;
        currencyApi = retrofit.create(CurrencyApi.class);

    }

    @Override
    public List<CurrencyDto> getAll() {

        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        Call<List<CurrencyDto>> currencyDtoListCall = currencyApi.getAll(currencyUrl);

        try {
            currencyDtoList.addAll(currencyDtoListCall.execute().body());
            log.info("Успешно выполнен запрос на получение списка CurrencyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка CurrencyDto - {}", e);
        }

        return currencyDtoList;
    }

    @Override
    public CurrencyDto getById(Long id) {

        CurrencyDto currencyDto = null;
        Call<CurrencyDto> currencyDtoCall = currencyApi.getById(currencyUrl, id);

        try {
            currencyDto = currencyDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра CurrencyDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра CurrencyDto по id= {} - {}",
                    id, e);
        }

        return currencyDto;
    }

    @Override
    public void create(CurrencyDto currencyDto) {

        Call<Void> currencyDtoCall = currencyApi.create(currencyUrl, currencyDto);

        try {
            currencyDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра CurrencyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра CurrencyDto - {}", e);
        }

    }

    @Override
    public void update(CurrencyDto currencyDto) {

        Call<Void> currencyDtoList = currencyApi.update(currencyUrl, currencyDto);

        try {
            currencyDtoList.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра CurrencyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра CurrencyDto - {}", e);
        }

    }

    @Override
    public void deleteById(Long id) {

        Call<Void> currencyDtoCall = currencyApi.deleteById(currencyUrl, id);

        try {
            currencyDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра CurrencyDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра CurrencyDto с id= {} - {}", e);
        }

    }
}

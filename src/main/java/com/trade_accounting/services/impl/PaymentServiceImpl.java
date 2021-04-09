package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.api.PaymentApi;
import lombok.extern.slf4j.Slf4j;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentApi paymentApi;
    private final String paymentUrl;

    public PaymentServiceImpl(@Value("${payment_url}") String paymentUrl, Retrofit retrofit) {
        paymentApi = retrofit.create(PaymentApi.class);
        this.paymentUrl = paymentUrl;
    }

    @Override
    public List<PaymentDto> getAll() {
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        Call<List<PaymentDto>> paymentDtoListCall = paymentApi.getAll(paymentUrl);

        try {
            paymentDtoList = paymentDtoListCall.execute().body();
            Objects.requireNonNull(paymentDtoList).forEach(payment -> payment.setTime(payment.getTime()));
            log.info("Успешно выполнен запрос на получение списка PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PaymentDto - {}", e);
        }
        return paymentDtoList;
    }

    @Override
    public PaymentDto getById(Long id) {
        PaymentDto paymentDto = null;
        Call<PaymentDto> paymentDtoCall = paymentApi.getById(paymentUrl, id);

        try {
            paymentDto = paymentDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра PaymentDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра PaymentDto по id= {} - {}", id, e);
        }
        return paymentDto;
    }

    @Override
    public void create(PaymentDto paymentDto) {

        Call<Void> paymentDtoCall = paymentApi.create(paymentUrl, paymentDto);

        try {
            paymentDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра PaymentDto - {}", e);
        }
    }

    @Override
    public void update(PaymentDto paymentDto) {

        Call<Void> paymentDtoCall = paymentApi.update(paymentUrl, paymentDto);

        try {
            paymentDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра PaymentDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> paymentDtoCall = paymentApi.deleteById(paymentUrl, id);

        try {
            paymentDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра PaymentDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра PaymentDto по id= {} - {}", id, e);
        }
    }

    @Override
    public List<PaymentDto> search(String request) {
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        Call<List<PaymentDto>> paymentDtoListCall = paymentApi.search(paymentUrl, request);

        try {
            paymentDtoList = paymentDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск PaymentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск PaymentDto - {}", e);
        }
        return paymentDtoList;
    }
}

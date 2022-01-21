package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.PurchaseForecastDto;
import com.trade_accounting.services.interfaces.PurchaseForecastService;
import com.trade_accounting.services.interfaces.api.PurchaseForecastApi;
import lombok.extern.slf4j.Slf4j;
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
public class PurchaseForecastServiceImpl implements PurchaseForecastService {
    private final PurchaseForecastApi purchaseForecastApi;
    private final String purchaseForecastUrl;
    private final CallExecuteService<PurchaseForecastDto> callExecuteService;

    public PurchaseForecastServiceImpl(@Value("${purchase_forecast_url}") String purchaseForecastUrl, Retrofit retrofit,
                                       CallExecuteService<PurchaseForecastDto> callExecuteService) {
        purchaseForecastApi = retrofit.create(PurchaseForecastApi.class);
        this.purchaseForecastUrl = purchaseForecastUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<PurchaseForecastDto> getAll() {
        List<PurchaseForecastDto> getAllPurchaseForecast = new ArrayList<>();
        Call<List<PurchaseForecastDto>> getAllPurchaseForecastCall = purchaseForecastApi.getAll(purchaseForecastUrl);

        try {
            getAllPurchaseForecast = getAllPurchaseForecastCall.execute().body();
            log.info("Успешно выполнен запрос на получение PurchaseForecastDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнения запроса на получение списка PurchaseForecastDto - {IOException}");
        }
        return getAllPurchaseForecast;
    }

    @Override
    public PurchaseForecastDto getById(Long id) {
        PurchaseForecastDto purchaseForecastDto = null;
        Call<PurchaseForecastDto> purchaseForecastDtoCall = purchaseForecastApi.getById(purchaseForecastUrl, id);

        try {
            purchaseForecastDto = purchaseForecastDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра purchaseForecastDto с id = -{}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра purchaseForecastDto по id = {} - {}", id, e);
        }
        return purchaseForecastDto;
    }

    @Override
    public void create(PurchaseForecastDto purchaseCurrentBalanceDto) {

    }

    @Override
    public void update(PurchaseForecastDto purchaseCurrentBalanceDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<PurchaseForecastDto> search(String query) {
        return null;
    }

    @Override
    public List<PurchaseForecastDto> searchByFilter(Map<String, String> query) {
        return null;
    }
}

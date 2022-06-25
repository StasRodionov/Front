package com.trade_accounting.components.apps.impl.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseHistoryOfSalesDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.purchases.PurchaseHistoryOfSalesService;
import com.trade_accounting.services.api.purchases.PurchaseHistoryOfSalesApi;
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
public class PurchaseHistoryOfSalesServiceImpl implements PurchaseHistoryOfSalesService {
    private final PurchaseHistoryOfSalesApi purchaseHistoryOfSales;
    private final String purchaseHistoryOfSalesUrl;
    private final CallExecuteService<PurchaseHistoryOfSalesDto> callExecuteService;


    public PurchaseHistoryOfSalesServiceImpl(@Value("${purchase_history_of_sales_url}") String purchaseHistoryOfSalesUrl, Retrofit retrofit,
                                      CallExecuteService<PurchaseHistoryOfSalesDto> callExecuteService) {
        purchaseHistoryOfSales = retrofit.create(PurchaseHistoryOfSalesApi.class);
        this.purchaseHistoryOfSalesUrl = purchaseHistoryOfSalesUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<PurchaseHistoryOfSalesDto> getAll() {
        List<PurchaseHistoryOfSalesDto> getAllHistoryOfSales = new ArrayList<>();
        Call<List<PurchaseHistoryOfSalesDto>> getAllHistoryOfSalesCall = purchaseHistoryOfSales.getAll(purchaseHistoryOfSalesUrl);

        try {
            getAllHistoryOfSales = getAllHistoryOfSalesCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PurchaseHistoryOfSalesDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PurchaseHistoryOfSalesDto - {IOException}", e);
        }
        return getAllHistoryOfSales;

    }

    @Override
    public PurchaseHistoryOfSalesDto getById(Long id) {
        PurchaseHistoryOfSalesDto purchaseHistoryOfSalesDto = null;
        Call<PurchaseHistoryOfSalesDto> purchaseHistoryOfSalesDtoCall = purchaseHistoryOfSales.getById(purchaseHistoryOfSalesUrl, id);

        try {
            purchaseHistoryOfSalesDto = purchaseHistoryOfSalesDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра purchaseHistoryOfSalesDto с id = -{}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра purchaseHistoryOfSalesDto по id= {} - {}", id, e);
        }
        return purchaseHistoryOfSalesDto;
    }

    @Override
    public void create(PurchaseHistoryOfSalesDto purchaseHistoryOfSalesDto) {

    }

    @Override
    public void update(PurchaseHistoryOfSalesDto purchaseHistoryOfSalesDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<PurchaseHistoryOfSalesDto> search(String query) {
        Call<List<PurchaseHistoryOfSalesDto>> getHistoryOfSalesByNameFilter = purchaseHistoryOfSales
                .searchByString(purchaseHistoryOfSalesUrl, query);
        List<PurchaseHistoryOfSalesDto> historyOfSalesDto = new ArrayList<>();
        try {
            historyOfSalesDto = getHistoryOfSalesByNameFilter.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение PurchaseHistoryOfSalesDto по фильтру -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение PurchaseHistoryOfSalesDto -{IOException}", e);
        }
        return historyOfSalesDto;
    }

    @Override
    public List<PurchaseHistoryOfSalesDto> searchByFilter(Map<String, String> query) {
        return null;
    }
}

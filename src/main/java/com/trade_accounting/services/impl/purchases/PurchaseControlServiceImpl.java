package com.trade_accounting.services.impl.purchases;

import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.purchases.PurchaseControlDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.purchases.PurchaseControlService;
import com.trade_accounting.services.api.purchases.PurchaseControlApi;
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
public class PurchaseControlServiceImpl implements PurchaseControlService {

    private final PurchaseControlApi purchaseControlApi;
    private final String purchaseControlUrl;
    private final CallExecuteService<PurchaseControlDto> callExecuteService;


    public PurchaseControlServiceImpl(@Value("${purchase_control_url}") String purchaseControlUrl, Retrofit retrofit,
                                      CallExecuteService<PurchaseControlDto> callExecuteService) {
        purchaseControlApi = retrofit.create(PurchaseControlApi.class);
        this.purchaseControlUrl = purchaseControlUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<PurchaseControlDto> getAll() {
        List<PurchaseControlDto> getAllPurchaseControl = new ArrayList<>();
        Call<List<PurchaseControlDto>> getAllPurchaseControlCall = purchaseControlApi.getAll(purchaseControlUrl);

        try {
            getAllPurchaseControl = getAllPurchaseControlCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PurchaseControlDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PurchaseControlDto - {IOException}", e);
        }
        return getAllPurchaseControl;

    }

    @Override
    public PurchaseControlDto getById(Long id) {
        PurchaseControlDto purchaseControlDto = null;
        Call<PurchaseControlDto> purchaseControlDtoCall = purchaseControlApi.getById(purchaseControlUrl, id);

        try {
            purchaseControlDto = purchaseControlDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра purchaseControlDto с id = -{}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра purchaseControlDto по id= {} - {}", id, e);
        }
        return purchaseControlDto;
    }

    @Override
    public void create(PurchaseControlDto purchaseControlDto) {

    }

    @Override
    public void update(PurchaseControlDto purchaseControlDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<PurchaseControlDto> search(String query) {
        Call<List<PurchaseControlDto>> getPurchaseControlByNameFilter = purchaseControlApi.searchByString(purchaseControlUrl, query);
        List<PurchaseControlDto> purchaseControlDto = new ArrayList<>();
        try {
            purchaseControlDto = getPurchaseControlByNameFilter.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение PurchaseControlDto по фильтру -{}", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение PurchaseControlDto -{IOException}", e);
        }
        return purchaseControlDto;
    }

    @Override
    public List<PurchaseControlDto> searchByFilter(Map<String, String> query) {
        return null;
    }

    @Override
    public List<PurchaseControlDto> newFilter(/*Вставить что будем отправлять Map`у или же отдельными String`ами */ ) {
        List<PurchaseControlDto> purchaseControlDtoList = new ArrayList<>();
        Call<List<PurchaseControlDto>> purchaseControlDtoListCall = purchaseControlApi.newFilter(purchaseControlUrl, 1L
                                                                /*Убрать 1L. Вставить что будем отправлять Map`у или же отдельными String`ами */ );
        try {
            purchaseControlDtoList = purchaseControlDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение Управления закупками по фильтру {}", 1L /*Убрать 1L. Вставить что будем отправлять Map`у или же отдельными String`ами */);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение Управления закупками {IOException}", e);
        }
        return purchaseControlDtoList;
    }
}

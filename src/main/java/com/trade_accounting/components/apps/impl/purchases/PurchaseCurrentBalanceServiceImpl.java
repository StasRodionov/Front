package com.trade_accounting.components.apps.impl.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseCurrentBalanceDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.purchases.PurchaseCurrentBalanceService;
import com.trade_accounting.services.api.purchases.PurchaseCurrentBalanceApi;
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
public class PurchaseCurrentBalanceServiceImpl implements PurchaseCurrentBalanceService {
    private final PurchaseCurrentBalanceApi purchaseCurrentBalance;
    private final String purchaseCurrentBalanceUrl;
    private final CallExecuteService<PurchaseCurrentBalanceDto> callExecuteService;

    public PurchaseCurrentBalanceServiceImpl(@Value("${purchase_current_balance_url}") String purchaseCurrentBalanceUrl,
                                             Retrofit retrofit,
                                             CallExecuteService<PurchaseCurrentBalanceDto> callExecuteService) {
        purchaseCurrentBalance = retrofit.create(PurchaseCurrentBalanceApi.class);
        this.purchaseCurrentBalanceUrl = purchaseCurrentBalanceUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<PurchaseCurrentBalanceDto> getAll() {
        List<PurchaseCurrentBalanceDto> getAllCurrentBalance =new ArrayList<>();
        Call<List<PurchaseCurrentBalanceDto>> getAllCurrentBalanceCall = purchaseCurrentBalance.getAll(purchaseCurrentBalanceUrl);

        try {
            getAllCurrentBalance = getAllCurrentBalanceCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка PurchaseCurrentBalanceDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка PurchaseCurrentBalanceDto - {IOException}", e);
        }
        return getAllCurrentBalance;
    }

    @Override
    public PurchaseCurrentBalanceDto getById(Long id) {
        PurchaseCurrentBalanceDto purchaseCurrentBalanceDto = null;
        Call<PurchaseCurrentBalanceDto> purchaseCurrentBalanceDtoCall = purchaseCurrentBalance.getById(purchaseCurrentBalanceUrl, id);

        try {
            purchaseCurrentBalanceDto = purchaseCurrentBalanceDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра purchaseCurrentBalanceDto с id = -{}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра purchaseCurrentBalanceDto по id= {} - {}", id, e);
        }
        return purchaseCurrentBalanceDto;
    }

    @Override
    public void create(PurchaseCurrentBalanceDto purchaseCurrentBalanceDto) {

    }

    @Override
    public void update(PurchaseCurrentBalanceDto purchaseCurrentBalanceDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<PurchaseCurrentBalanceDto> search(String query) {
        return null;
    }

    @Override
    public List<PurchaseCurrentBalanceDto> searchByFilter(Map<String, String> query) {
        return null;
    }
}

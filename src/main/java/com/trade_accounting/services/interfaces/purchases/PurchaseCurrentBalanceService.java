package com.trade_accounting.services.interfaces.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseCurrentBalanceDto;

import java.util.List;
import java.util.Map;

public interface PurchaseCurrentBalanceService {

    List<PurchaseCurrentBalanceDto> getAll();

    PurchaseCurrentBalanceDto getById(Long id);

    void create(PurchaseCurrentBalanceDto purchaseCurrentBalanceDto);

    void update(PurchaseCurrentBalanceDto purchaseCurrentBalanceDto);

    void deleteById(Long id);

    List<PurchaseCurrentBalanceDto> search(String query);

    List<PurchaseCurrentBalanceDto> searchByFilter(Map<String, String> query);
}

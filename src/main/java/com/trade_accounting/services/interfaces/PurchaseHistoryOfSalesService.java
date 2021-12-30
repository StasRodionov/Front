package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PurchaseHistoryOfSalesDto;

import java.util.List;
import java.util.Map;

public interface PurchaseHistoryOfSalesService {

    List<PurchaseHistoryOfSalesDto> getAll();

    PurchaseHistoryOfSalesDto getById(Long id);

    void create(PurchaseHistoryOfSalesDto purchaseHistoryOfSalesDto);

    void update(PurchaseHistoryOfSalesDto purchaseHistoryOfSalesDto);

    void deleteById(Long id);

    List<PurchaseHistoryOfSalesDto> search(String query);

    List<PurchaseHistoryOfSalesDto> searchByFilter(Map<String, String> query);
}

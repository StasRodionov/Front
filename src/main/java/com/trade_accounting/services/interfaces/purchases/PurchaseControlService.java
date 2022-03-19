package com.trade_accounting.services.interfaces.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseControlDto;

import java.util.List;
import java.util.Map;

public interface PurchaseControlService {

    List<PurchaseControlDto> getAll();

//    List<PurchaseControlDto> getAll(String purchaseControl);

    PurchaseControlDto getById(Long id);

    void create(PurchaseControlDto purchaseControlDto);

    void update(PurchaseControlDto purchaseControlDto);

    void deleteById(Long id);

    List<PurchaseControlDto> search(String query);

    List<PurchaseControlDto> searchByFilter(Map<String, String> query);
}

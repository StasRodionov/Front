package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailReturnsDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;

import java.util.List;
import java.util.Map;

public interface RetailStoreService {

    List<RetailStoreDto> getAll();

    RetailStoreDto getById(Long id);

    void create(RetailStoreDto retailStoreDto);

    void update(RetailStoreDto retailStoreDto);

    void deleteById(Long id);

    List<RetailStoreDto> search(String query);

    List<RetailStoreDto> searchRetailStoreByFilter(Map<String, String> queryRetailStore);

}

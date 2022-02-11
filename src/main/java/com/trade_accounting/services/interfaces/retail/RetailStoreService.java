package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailStoreDto;

import java.util.List;

public interface RetailStoreService {

    List<RetailStoreDto> getAll();

    RetailStoreDto getById(Long id);

    void create(RetailStoreDto retailStoreDto);

    void update(RetailStoreDto retailStoreDto);

    void deleteById(Long id);

}

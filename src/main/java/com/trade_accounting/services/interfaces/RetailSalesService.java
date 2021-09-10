package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailSalesDto;
import com.trade_accounting.models.dto.RetailStoreDto;

import java.util.List;

public interface RetailSalesService {

    List<RetailSalesDto> getAll();

    RetailSalesDto getById(Long id);

    void create(RetailSalesDto retailSalesDto);

    void update(RetailSalesDto retailSalesDto);

    void deleteById(Long id);

}

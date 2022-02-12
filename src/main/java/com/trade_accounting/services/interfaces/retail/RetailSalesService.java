package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailSalesDto;

import java.util.List;
import java.util.Map;

public interface RetailSalesService {

    List<RetailSalesDto> getAll();

    RetailSalesDto getById(Long id);

    void create(RetailSalesDto retailSalesDto);

    void update(RetailSalesDto retailSalesDto);

    void deleteById(Long id);

    List<RetailSalesDto> search(String query);

    List<RetailSalesDto> searchRetailSales(Map<String, String> queryRetailSales);
}

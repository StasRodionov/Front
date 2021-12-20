package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceToBuyerListProductsDto;

import java.util.List;
import java.util.Map;

public interface InvoiceToBuyerListProductsService {
    List<InvoiceToBuyerListProductsDto> getAll();

    InvoiceToBuyerListProductsDto getById(Long id);

    List<InvoiceToBuyerListProductsDto> getBySupplierId(Long id);

    void create(InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto);

    void update(InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto);

    void deleteById(Long id);

    List<InvoiceToBuyerListProductsDto> search(Map<String, String> query);
}
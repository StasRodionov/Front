package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.SupplierAccountProductsListDto;

import java.util.List;
import java.util.Map;

public interface SupplierAccountProductsListService {
    List<SupplierAccountProductsListDto> getAll();

    SupplierAccountProductsListDto getById(Long id);

    List<SupplierAccountProductsListDto> getBySupplierId(Long id);

    void create(SupplierAccountProductsListDto supplierAccountProductsListDto);

    void update(SupplierAccountProductsListDto supplierAccountProductsListDto);

    void deleteById(Long id);

    List<SupplierAccountProductsListDto> search(Map<String, String> query);
}

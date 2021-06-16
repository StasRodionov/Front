package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.SupplierAccountDto;
import retrofit2.Response;

import java.util.List;

public interface SupplierAccountService {

    List<SupplierAccountDto> getAll();

    SupplierAccountDto getById(Long id);

    List<SupplierAccountDto> searchByFilter(String nameFilter);

    Response<SupplierAccountDto> create(SupplierAccountDto supplierAccountDto);

    void update(SupplierAccountDto supplierAccountDto);

    void deleteById(Long id);
}

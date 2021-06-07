package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.SupplierAccountsDto;
import retrofit2.Response;

import java.util.List;

public interface SupplierAccountService {

    List<SupplierAccountsDto> getAll();

    SupplierAccountsDto getById(Long id);

    Response<SupplierAccountsDto> create(SupplierAccountsDto supplierAccountsDto);

    void update(SupplierAccountsDto supplierAccountsDto);

    void deleteById(Long id);
}

package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

public interface SupplierAccountService {

    List<SupplierAccountDto> getAll();

    List<SupplierAccountDto> getAll(String typeOfInvoice);

    SupplierAccountDto getById(Long id);

    List<SupplierAccountDto> searchByString(String nameFilter);

    Response<SupplierAccountDto> create(SupplierAccountDto supplierAccountDto);

    void update(SupplierAccountDto supplierAccountDto);

    void deleteById(Long id);

    List<SupplierAccountDto> searchByFilter(Map<String,String> querySupplier);

    List<SupplierAccountDto> findBySearchAndTypeOfInvoice(String search, String typeOfInvoice);
}

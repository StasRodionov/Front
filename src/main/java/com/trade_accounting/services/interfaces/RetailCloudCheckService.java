package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.RetailCloudCheckDto;

import java.util.List;
import java.util.Map;

public interface RetailCloudCheckService {

    List<RetailCloudCheckDto> getAll();

    RetailCloudCheckDto getById(Long id);

    List<RetailCloudCheckDto> search(Map<String, String> query);

    void create(RetailCloudCheckDto retailCloudCheckDto);

    void update(RetailCloudCheckDto retailCloudCheckDto);

    void deleteById(Long id);

}

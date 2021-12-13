package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailCloudCheckDto;

import java.util.List;

public interface RetailCloudCheckService {

    List<RetailCloudCheckDto> getAll();

    RetailCloudCheckDto getById(Long id);

    void create(RetailCloudCheckDto retailCloudCheckDto);

    void update(RetailCloudCheckDto retailCloudCheckDto);

    void deleteById(Long id);

}

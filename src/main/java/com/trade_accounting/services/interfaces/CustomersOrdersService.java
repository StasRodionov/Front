package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CustomersOrdersDto;
import com.trade_accounting.models.dto.EmployeeDto;

import java.util.List;

public interface CustomersOrdersService {

    List<CustomersOrdersDto> getAll();

    CustomersOrdersDto getById(Long id);

    void create(CustomersOrdersDto customersOrdersDto);

    void update(CustomersOrdersDto customersOrdersDto);

    void deleteById(Long id);
}

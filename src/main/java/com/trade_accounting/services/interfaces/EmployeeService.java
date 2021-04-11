package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.EmployeeDto;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<EmployeeDto> getAll();

    List<EmployeeDto> search(Map<String, String> query);

    EmployeeDto getById(Long id);

    void create(EmployeeDto employeeDto);

    void update(EmployeeDto employeeDto);

    void deleteById(Long id);

    List<EmployeeDto> searchBySymbols(String symbols);
}

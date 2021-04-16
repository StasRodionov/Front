package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.EmployeeDto;

public interface EmployeeService extends PageableService<EmployeeDto> {

    EmployeeDto getById(Long id);

    void create(EmployeeDto employeeDto);

    void update(EmployeeDto employeeDto);

    void deleteById(Long id);
}

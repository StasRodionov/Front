package com.trade_accounting.services.interfaces.client;

import com.trade_accounting.models.dto.client.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> getAll();

    DepartmentDto getById(Long id);

    void create(DepartmentDto departmentDto);

    void update(DepartmentDto departmentDto);

    void deleteById(Long id);
}

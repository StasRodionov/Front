package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    List<CompanyDto> getAll();

    CompanyDto getById(String id);

    void add(CompanyDto companyDto);

    void update(CompanyDto companyDto);

    void deleteById(String id);

}

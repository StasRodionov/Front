package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.CompanyDto;

import java.util.List;
import java.util.Map;

public interface CompanyService {

    List<CompanyDto> getAll();

    List<CompanyDto> search(Map<String, String> query);

    CompanyDto getById(Long id);

    CompanyDto getByEmail(String email);

    void create(CompanyDto companyDto);

    void update(CompanyDto companyDto);

    void deleteById(Long id);

}

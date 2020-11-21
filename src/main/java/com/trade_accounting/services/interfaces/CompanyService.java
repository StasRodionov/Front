package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getCompanies();
    CompanyDto getCompany(String id);
    void addCompany(CompanyDto companyDto);
    void deleteCompany(String id);
}

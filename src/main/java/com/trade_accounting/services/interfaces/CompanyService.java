package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    List<CompanyDto> getCompanies();

    CompanyDto getCompany(String id);

    void addCompany(CompanyDto companyDto);

    void updateCompany(CompanyDto companyDto);

    void deleteCompany(String id);

}

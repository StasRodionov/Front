package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class CompanyServiceImpl {

    @Value("company_url")
    private static String COMPANY_URL;

    private Retrofit retrofitClient;

    CompanyApi companyApi = retrofitClient.create(CompanyApi.class);

    Response<List<CompanyDto>> companies = companyApi.getCompanies(COMPANY_URL);

    Response<CompanyDto> company = companyApi.getCompany(COMPANY_URL, "");

    Response<CompanyDto> addedCompany = companyApi.addCompany(COMPANY_URL, new CompanyDto());

    Response<CompanyDto> deletedCompany = companyApi.deleteCompany(COMPANY_URL, "");

}

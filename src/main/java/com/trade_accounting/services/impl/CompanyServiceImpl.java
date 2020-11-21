package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${company_url}")
    private static String COMPANY_URL;

    private Retrofit retrofitClient;

    @Autowired
    public void setRetrofitClient(Retrofit retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    private CompanyApi companyApi = retrofitClient.create(CompanyApi.class);

    public List<CompanyDto> getCompanies() {
        Response<List<CompanyDto>> companies = companyApi.getCompanies(COMPANY_URL);
        return companies.body();
    }

    public CompanyDto getCompany(String id) {
        Response<CompanyDto> company = companyApi.getCompany(COMPANY_URL, "");
        return company.body();
    }

    public void addCompany(CompanyDto companyDto) {
        Response<CompanyDto> addedCompany = companyApi.addCompany(COMPANY_URL, companyDto);
    }

    public void deleteCompany(String id) {
        Response<CompanyDto> deletedCompany = companyApi.deleteCompany(COMPANY_URL, id);
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyApi;
import com.trade_accounting.services.interfaces.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {

    @Value("${company_url}")
    private static String COMPANY_URL;

    private Retrofit retrofitClient;

    private CompanyApi companyApi = retrofitClient.create(CompanyApi.class);

    @Autowired
    public void setRetrofitClient(Retrofit retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    @Override
    public List<CompanyDto> getCompanies() {
        List<CompanyDto> companies = null;

        try {
            companies = companyApi.getCompanies(COMPANY_URL).body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public CompanyDto getCompany(String id) {
        CompanyDto company = null;

        try {
            company = companyApi.getCompany(COMPANY_URL, "").body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public void addCompany(CompanyDto companyDto) {
        try {
            Response<CompanyDto> addedCompany = companyApi.addCompany(COMPANY_URL, companyDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(String id) {
        try {
            Response<CompanyDto> deletedCompany = companyApi.deleteCompany(COMPANY_URL, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

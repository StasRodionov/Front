package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyApi;
import com.trade_accounting.services.interfaces.CompanyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private String companyUrl;

    private CompanyApi companyApi;

    @Autowired
    public CompanyServiceImpl(Retrofit retrofitClient, Environment environment) {

        companyUrl = environment.getProperty("company_url");

        companyApi = retrofitClient.create(CompanyApi.class);

    }

    @Override
    public List<CompanyDto> getCompanies() {
        List<CompanyDto> companyDtoList = null;

        try {
            companyDtoList = companyApi.getCompanies(companyUrl).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companyDtoList;
    }

    @Override
    public CompanyDto getCompany(String id) {
        CompanyDto companyDto = null;

        try {
            companyDto = companyApi.getCompany(companyUrl, id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companyDto;
    }

    @Override
    public void addCompany(CompanyDto companyDto) {
        try {
            Response<CompanyDto> addedCompany = companyApi.addCompany(companyUrl, companyDto).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCompany(CompanyDto companyDto) {
        try {
            Response<CompanyDto> updatedCompany = companyApi.updateCompany(companyUrl, companyDto).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(String id) {
        try {
            Response<CompanyDto> deletedCompany = companyApi.deleteCompany(companyUrl, id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface CompanyApi {

    @GET("{url}")
    Response<List<CompanyDto>> getCompanies(@Path("url") String url);

    @GET("{url}/{id}")
    Response<CompanyDto> getCompany(@Path("url") String url, @Path("id") String id);

    @POST("{url}")
    Response<CompanyDto> addCompany(@Path("url") String url, @Body CompanyDto companyDto);

    @DELETE("{url}/{id}")
    Response<CompanyDto> deleteCompany(@Path("url") String url, @Path("id") String id);
}

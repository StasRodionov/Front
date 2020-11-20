package com.trade_accounting.services.interfaces;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CompanyAPI {
    @GET("/api/company")
    Response<?> getCompanies();

    @GET("/api/company/{id}")
    Response<?> getCompany(@Path("id") String id);

    //TODO add @Body CompanyDTO companyDTO after their creation to the methods args
    @POST("/api/company")
    Response<?> addCompany();

    @DELETE("/api/company/{id}")
    Response<?> deleteCompany(@Path("id") String id);
}

package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CompanyDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface CompanyApi {

    @Headers("Accept: application/json")
    @GET("")
    Call<List<CompanyDto>> getAll();

    @Headers("Accept: application/json")
    @GET("{id}")
    Call<CompanyDto> getById(@Path("url") String id);

    @Headers("Accept: application/json")
    @POST("")
    Call<CompanyDto> add(@Body CompanyDto companyDto);

    @Headers("Accept: application/json")
    @PUT("")
    Call<CompanyDto> update(@Body CompanyDto companyDto);

    @Headers("Accept: application/json")
    @DELETE("{id}")
    Call<CompanyDto> deleteById(@Path("id") String id);
}

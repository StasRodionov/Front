package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.ProductDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ProductApi {


    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ProductDto>> getAll(@Path(value = "url", encoded = true) String url);


    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call <ProductDto> getById(@Path(value = "url", encoded = true) String url, @Path(value="id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call <Void> create(@Path(value = "url", encoded = true) String url, @Body ProductDto productDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body ProductDto productDto);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call <Void> deleteById(@Path(value = "url", encoded = true) String url, @Path(value="id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/pg/{id}")
    Call<List<ProductDto>> getAllByProductGroup(@Path(value = "url", encoded = true) String url, @Path(value="id") Long id);}

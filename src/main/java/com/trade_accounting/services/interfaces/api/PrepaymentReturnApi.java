package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.PrepaymentReturnDto;
import com.trade_accounting.models.dto.PrepayoutDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface PrepaymentReturnApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<PrepaymentReturnDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<PrepaymentReturnDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<PrepaymentReturnDto> create(@Path(value = "url", encoded = true) String url, @Body PrepaymentReturnDto prepaymentReturnDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body PrepaymentReturnDto prepaymentReturnDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

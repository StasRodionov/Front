package com.trade_accounting.services.api.finance;

import com.trade_accounting.models.dto.finance.PrepayoutDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PrepayoutApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<PrepayoutDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<PrepayoutDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<PrepayoutDto> create(@Path(value = "url", encoded = true) String url, @Body PrepayoutDto prepayoutDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body PrepayoutDto prepayoutDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

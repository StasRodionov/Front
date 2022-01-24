package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.controllers.dto.OperationsDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface OperationsApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<OperationsDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<OperationsDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

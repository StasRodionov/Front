package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.StatusDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface StatusApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<StatusDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call <StatusDto> getById(@Path(value = "url", encoded = true) String url, @Path(value="id") Long id);
}

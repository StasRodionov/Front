package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.OperationsDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface OperationsApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<OperationsDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<OperationsDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/queryOperations")
    Call<List<OperationsDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                           @QueryMap Map<String, String> queryOperations);

    @Headers("Accept: application/json")
    @GET("{url}/quickSearch")
    Call<List<OperationsDto>> quickSearch(@Path(value = "url", encoded = true) String url,
                                       @Query("search") String text);
}

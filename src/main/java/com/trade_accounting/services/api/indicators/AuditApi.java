package com.trade_accounting.services.api.indicators;

import com.trade_accounting.models.dto.indicators.AuditDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface AuditApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<AuditDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/quickSearch")
    Call<List<AuditDto>> quickSearch(@Path(value = "url", encoded = true) String url,
                                          @Query("search") String text);

    @Headers("Accept: application/json")
    @GET("{url}/queryOperations")
    Call<List<AuditDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                             @QueryMap Map<String, String> queryOperations);

}
package com.trade_accounting.services.api.indicators;

import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.models.dto.warehouse.RemainDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body AuditDto auditDto);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<AuditDto> create(@Path(value = "url", encoded = true) String url, @Body AuditDto auditDto);
}
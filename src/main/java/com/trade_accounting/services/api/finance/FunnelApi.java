package com.trade_accounting.services.api.finance;

import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface FunnelApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<FunnelDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<FunnelDto>> searchByFilter(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> filterData);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<FunnelDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

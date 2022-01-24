package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.controllers.dto.MoneySubMutualSettlementsDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface MoneySubMutualSettlementsApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<MoneySubMutualSettlementsDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body MoneySubMutualSettlementsDto moneySubMutualSettlementsDto);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<MoneySubMutualSettlementsDto>> searchByFilter(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> filterData);

}

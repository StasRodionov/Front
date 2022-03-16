package com.trade_accounting.services.api.warehouse;

import com.trade_accounting.models.dto.warehouse.SerialNumbersDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;


public interface SerialNumbersApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<SerialNumbersDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<SerialNumbersDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<SerialNumbersDto> create(@Path(value = "url", encoded = true) String url, @Body SerialNumbersDto serialNumbersDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body SerialNumbersDto serialNumbersDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<SerialNumbersDto>> searchByFilter(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> filterData);
}

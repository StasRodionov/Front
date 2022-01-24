package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.controllers.dto.AcceptanceProductionDto;
import com.trade_accounting.controllers.dto.RetailShiftDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface AcceptanceProductionApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<AcceptanceProductionDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<AcceptanceProductionDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<AcceptanceProductionDto> create(@Path(value = "url", encoded = true) String url, @Body AcceptanceProductionDto acceptanceProductionDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body AcceptanceProductionDto acceptanceProductionDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<AcceptanceProductionDto>> search(@Path(value = "url", encoded = true) String url, @Query("query") String query);
}

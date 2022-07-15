package com.trade_accounting.services.api.warehouse;

import com.trade_accounting.models.dto.warehouse.ShipmentDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface ShipmentApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ShipmentDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ShipmentDto>> getAll(@Path(value = "url", encoded = true) String url, @Query("typeOfInvoice") String typeOfInvoice);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<ShipmentDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body ShipmentDto shipmentDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body ShipmentDto shipmentDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @PUT("{url}/moveToIsRecyclebin/{id}")
    Call<Void> moveToIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @PUT("{url}/restoreFromIsRecyclebin/{id}")
    Call<Void> restoreFromIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/queryShipment")
    Call<List<ShipmentDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                                  @QueryMap Map<String, String> queryShipment);

    @Headers("Accept: application/json")
    @GET("{url}/search/{search}")
    Call<List<ShipmentDto>> searchByString(@Path(value = "url", encoded = true) String url,
                                           @Path(value = "search", encoded = true) String search);

}

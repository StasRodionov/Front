package com.trade_accounting.services.api.purchases;

import com.trade_accounting.models.dto.purchases.PurchaseControlDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface PurchaseControlApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<PurchaseControlDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/search/{nameFilter}")
    Call<List<PurchaseControlDto>> searchByString(@Path(value = "url", encoded = true) String url,
                                                  @Path(value = "nameFilter", encoded = true) String nameFilter);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<PurchaseControlDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body PurchaseControlDto purchaseControlDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body PurchaseControlDto purchaseControlDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

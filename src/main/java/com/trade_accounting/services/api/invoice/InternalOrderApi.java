package com.trade_accounting.services.api.invoice;

import com.trade_accounting.models.dto.invoice.InternalOrderDto;
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

public interface InternalOrderApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InternalOrderDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")   // добавил
    @GET("{url}/search/{searchItem}")
    Call<List<InternalOrderDto>> getAll(@Path(value = "url", encoded = true) String url, @Path("searchItem") String searchItem);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InternalOrderDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<InternalOrderDto> create(@Path(value = "url", encoded = true) String url, @Body InternalOrderDto internalOrderDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InternalOrderDto internalOrderDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<InternalOrderDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                                @QueryMap Map<String, String> query);

    @Headers("Accept: application/json")
    @GET("{url}/searchByBetweenDataFilter")
    Call<List<InternalOrderDto>> searchByBetweenDataFilter(@Path(value = "url", encoded = true) String url,
                                                           @QueryMap Map<String, String> query);

    @Headers("Accept: application/json")
    @PUT("{url}/moveToIsRecyclebin/{id}")
    Call<Void> moveToIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @PUT("{url}/restoreFromIsRecyclebin/{id}")
    Call<Void> restoreFromIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

}

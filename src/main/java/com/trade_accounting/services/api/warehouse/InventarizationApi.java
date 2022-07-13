package com.trade_accounting.services.api.warehouse;

import com.trade_accounting.models.dto.warehouse.InventarizationDto;
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

public interface InventarizationApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InventarizationDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InventarizationDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body InventarizationDto inventarizationDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InventarizationDto inventarizationDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<InventarizationDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                                  @QueryMap Map<String, String> queryInventarization);

    @Headers("Accept: application/json")
    @GET("{url}/searchByBetweenDataFilter")
    Call<List<InventarizationDto>> searchByBetweenDataFilter(@Path(value = "url", encoded = true) String url,
                                                  @QueryMap Map<String, String> queryInventarization);

    @Headers("Accept: application/json")
    @GET("{url}/search/{search}")
    Call<List<InventarizationDto>> search(@Path(value = "url", encoded = true) String url,
                                          @Path(value = "search", encoded = true) String search);

    @Headers("Accept: application/json")
    @PUT("{url}/moveToIsRecyclebin/{id}")
    Call<Void> moveToIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @PUT("{url}/restoreFromIsRecyclebin/{id}")
    Call<Void> restoreFromIsRecyclebin(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

}

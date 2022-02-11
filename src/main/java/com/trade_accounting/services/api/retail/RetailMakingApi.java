package com.trade_accounting.services.api.retail;

import com.trade_accounting.models.dto.retail.RetailMakingDto;
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

public interface RetailMakingApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<RetailMakingDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<RetailMakingDto> getById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body RetailMakingDto retailMakingDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body RetailMakingDto retailMakingDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<RetailMakingDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                               @QueryMap Map<String, String> queryRetailMaking);

    @Headers("Accept: application/json")
    @GET("{url}/search/{search}")
    Call<List<RetailMakingDto>> search(@Path(value = "url", encoded = true) String url,
                                       @Path(value = "search", encoded = true) String search);
}

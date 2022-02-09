package com.trade_accounting.services.api.warehouse;

import com.trade_accounting.models.dto.warehouse.RemainDto;
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

public interface RemainApi {
     @Headers("Accept: application/json")
     @GET("{url}")
    Call<List<RemainDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<RemainDto>> getAll(@Path(value = "url", encoded = true) String url, @Query("typeOfRemain") String typeOfRemain);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<RemainDto> getById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<RemainDto> create(@Path(value = "url", encoded = true) String url, @Body RemainDto remainDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body RemainDto remainDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<RemainDto>> search(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> query);

    @Headers("Accept: application/json")
    @GET("{url}/searchByString")
    Call<List<RemainDto>> search(@Path(value = "url", encoded = true) String url,
                                  @Query("search") String search,
                                  @Query("typeOfRemain") String typeOfRemain);

}

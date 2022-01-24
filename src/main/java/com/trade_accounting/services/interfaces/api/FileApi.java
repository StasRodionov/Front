package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.controllers.dto.FileDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface FileApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<FileDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<FileDto> getById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<FileDto>> search(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> query);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body FileDto fileDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Long id);
}

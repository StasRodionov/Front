package com.trade_accounting.services.api.util;

import com.trade_accounting.models.dto.util.ColumnsMaskDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ColumnsMaskApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ColumnsMaskDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<ColumnsMaskDto> getById(@Path(value = "url", encoded = true) String url, @Path(value = "id") Integer id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body ColumnsMaskDto columnsMaskDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body ColumnsMaskDto columnsMaskDto);

}

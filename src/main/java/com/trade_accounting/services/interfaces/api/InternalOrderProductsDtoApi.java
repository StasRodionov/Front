package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.InternalOrderProductsDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface InternalOrderProductsDtoApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InternalOrderProductsDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<InternalOrderProductsDto> create(@Path(value = "url", encoded = true) String url, @Body InternalOrderProductsDto internalOrderProductsDto);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InternalOrderProductsDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}

package com.trade_accounting.services.api.production;

import com.trade_accounting.models.dto.production.OrdersOfProductionDto;
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

public interface OrdersOfProductionApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<OrdersOfProductionDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<OrdersOfProductionDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body OrdersOfProductionDto ordersOfProductionDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body OrdersOfProductionDto ordersOfProductionDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchOrdersOfProduction")
    Call<List<OrdersOfProductionDto>> searchOrdersOfProduction(@Path(value = "url", encoded = true) String url,
                                                               @QueryMap Map<String, String> queryOrdersOfProduction);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<OrdersOfProductionDto>> search(@Path(value = "url", encoded = true) String url, @Query("query") String query);
}

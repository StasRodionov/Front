package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.InvoiceToBuyerListProductsDto;
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

public interface InvoiceToBuyerListProductsApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InvoiceToBuyerListProductsDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InvoiceToBuyerListProductsDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<List<InvoiceToBuyerListProductsDto>> getBySupplierId(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search")
    Call<List<InvoiceToBuyerListProductsDto>> search(@Path(value = "url", encoded = true) String url,
                                                     @QueryMap Map<String, String> query);
}
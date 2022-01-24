package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.controllers.dto.ReturnAmountByProductDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReturnAmountByProductApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<ReturnAmountByProductDto> getTotalReturnAmountByProduct(
            @Path(value = "url", encoded = true) String url,
            @Query(value = "productId") Long productId,
            @Query(value = "invoiceId") Long invoiceId);
}

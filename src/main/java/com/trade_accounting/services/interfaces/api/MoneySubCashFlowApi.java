package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.MoneySubCashFlowDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.time.LocalDate;
import java.util.List;

public interface MoneySubCashFlowApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<MoneySubCashFlowDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body MoneySubCashFlowDto moneySubCashFlowDto);

    @Headers("Accept: application/json")
    @GET("{url}/filter")
    Call<List<MoneySubCashFlowDto>> filter(@Path(value = "url", encoded = true) String url, @Query("startDatePeriod") LocalDate startDatePeriod, @Query("endDatePeriod") LocalDate endDatePeriod, @Query("projectId") Long projectId, @Query("companyId") Long companyId, @Query("contractorId") Long contractorId);

}

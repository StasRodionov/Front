package com.trade_accounting.services.api.retail;

import com.trade_accounting.models.dto.retail.EventLogDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface RetailEventLogApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<EventLogDto>> getAll(@Path(value = "url", encoded = true) String url);
}

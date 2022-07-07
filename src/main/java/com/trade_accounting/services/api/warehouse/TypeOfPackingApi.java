package com.trade_accounting.services.api.warehouse;


import com.trade_accounting.models.dto.warehouse.TypeOfPackingDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface TypeOfPackingApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<TypeOfPackingDto>> getAll(@Path(value = "url", encoded = true) String url);
}

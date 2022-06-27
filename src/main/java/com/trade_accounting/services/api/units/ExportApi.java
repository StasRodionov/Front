package com.trade_accounting.services.api.units;

import com.trade_accounting.models.dto.units.ExportDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface ExportApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ExportDto>> getAll(@Path(value = "url", encoded = true) String url);
}

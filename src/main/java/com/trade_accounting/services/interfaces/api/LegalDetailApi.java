package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.LegalDetailDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface LegalDetailApi {

    @Headers("")
    @GET("{url}")
    Call<List<LegalDetailDto>> getAll(@Path("url") String url);

    @Headers("")
    @GET("{url}/{id}")
    Call<LegalDetailDto> getById(@Path("url") String url, @Path("id") Long id);

    @Headers("")
    @POST("{url}")
    Call<Void> create(@Path("url") String url, @Body LegalDetailDto legalDetailDto);

    @Headers("")
    @PUT("{url}")
    Call<Void> update(@Path("url") String url, @Body LegalDetailDto legalDetailDto);

    @Headers("")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path("url") String url, @Path("id") Long id);
}

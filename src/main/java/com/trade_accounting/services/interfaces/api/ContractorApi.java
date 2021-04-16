package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.ContractorDto;
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

public interface ContractorApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ContractorDto>> getAll(@Path(value = "url", encoded = true) String url);

//    @Headers("Accept: application/json")
//    @GET("{url}/lite")
//    Call<List<ContractorDto>> getAllLite(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/search/{searchTerm}")
    Call<List<ContractorDto>> getAll(@Path(value = "url", encoded = true) String url,@Path("searchTerm") String searchTerm);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<ContractorDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchContractor")
    Call<List<ContractorDto>> searchContractor(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> queryContractor);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<ContractorDto> create(@Path(value = "url", encoded = true) String url, @Body ContractorDto contractorDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<ContractorDto> update(@Path(value = "url", encoded = true) String url, @Body ContractorDto contractorDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<ContractorDto> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);


}

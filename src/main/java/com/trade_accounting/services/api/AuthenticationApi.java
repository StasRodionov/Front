package com.trade_accounting.services.api;

import com.trade_accounting.components.authentication.LoginRequest;
import com.trade_accounting.components.authentication.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static com.trade_accounting.config.SecurityConstants.LOGIN;

public interface AuthenticationApi {

    @POST(LOGIN)
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

}

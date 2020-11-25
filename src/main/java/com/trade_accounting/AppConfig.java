package com.trade_accounting;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Data
@Component
public class AppConfig {

    public Retrofit retrofit;

    public AppConfig(@Value("${base_url}") String baseUrl) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    }
}

package com.trade_accounting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;

@SpringBootApplication
public class TradeAccountingApplication {

    @Value("base_url")
    private static String BASE_URL;

    public static void main(String[] args) {
        SpringApplication.run(TradeAccountingApplication.class, args);
    }

    @Bean
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
    }

}

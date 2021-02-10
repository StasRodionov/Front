package com.trade_accounting.config;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

import static com.trade_accounting.config.SecurityConstants.*;


@Configuration
public class AppConfig {

    @Bean
    public OkHttpClient authorizationInterceptor() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
                        String token = (String) wrappedSession.getAttribute(TOKEN_ATTRIBUTE_NAME);

                        if (token != null) {
                            Request newRequest = originalRequest.newBuilder()
                                    .header(HEADER_STRING, TOKEN_PREFIX + token)
                                    .build();
                            return chain.proceed(newRequest);
                        } else {
                            return chain.proceed(originalRequest);
                        }
                    }
                })
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());

                        if (originalResponse.code() == 401) {
                            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

                            if (wrappedSession.getAttribute(TOKEN_ATTRIBUTE_NAME) != null) {
                                wrappedSession.removeAttribute(TOKEN_ATTRIBUTE_NAME);
                            }

                            return originalResponse;
                        } else {
                            return originalResponse;
                        }
                    }
                })
                .build();
    }

    @Bean
    public Retrofit retrofit(@Value("${base_url}") String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(authorizationInterceptor())
                .build();
    }
}

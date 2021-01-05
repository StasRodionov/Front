package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.api.ProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductServiceImp implements ProductService {

    private final String productUrl;
    private final ProductApi productApi;

     List<ProductDto> listProducts = new ArrayList<>();
     ProductDto productDto = new ProductDto();


    ProductServiceImp(@Value("${product_url}") String productUrl, Retrofit retrofit) {

        this.productUrl = productUrl;
        productApi = retrofit.create(ProductApi.class);
    }

    @Override
    public List<ProductDto> getAll() {

        Call<List<ProductDto>> productGetAllCall = productApi.getAll(productUrl);

        productGetAllCall.clone().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful()) {
                    listProducts.addAll(Objects.requireNonNull(response.body()));
                    log.info("Успешно выполнен запрос на получение списка ProductDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка ProductDto - {}",
                             response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка ProductDto", throwable);

            }
        });

        return listProducts;
    }



    @Override
    public ProductDto getById(Long id) {



        Call<ProductDto> productGetCall = productApi.getById(productUrl, id);

        try {
            productDto = productGetCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка CompanyDto - {}", e);
        }

//        productGetCall.clone().enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<ProductDto> call, Response<ProductDto> response) {
//                if (response.isSuccessful()) {
//                    productDto = response.body();
//                    log.info("Успешно выполнен запрос на получение ProductDto");
//                } else {
//                    log.error("Произошла ошибка при выполнении запроса на получение ProductDto - {}",
//                            response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductDto> call, Throwable throwable) {
//                log.error("Произошла ошибка при получении ответа на запрос  ProductDto", throwable);
//            }
//        });

        return productDto;
    }

    @Override
    public void create(ProductDto productDto) {
        Call<Void> productCall = productApi.create(productUrl, productDto);

        productCall.clone().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на добавлении ProductDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на добавлении ProductDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа при добавлении ProductDto", throwable);
            }
        });

    }

    @Override
    public void update(ProductDto productDto) {
        Call<Void> productUpdateCall = productApi.update(productUrl, productDto);
        productUpdateCall.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновлении ProductDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновлении ProductDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа при обновлении ProductDto", throwable);

            }
        });

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productDeleteCall = productApi.deleteById(productUrl, id);
        productDeleteCall.clone().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на  удалении ProductDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удалении ProductDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа при удалении ProductDto", throwable);

            }
        });
    }
}

package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.api.ProductGroupApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;


@Service
@Slf4j
public class ProductGroupServiceImpl implements ProductGroupService {

    private final String productGroupUrl;

    private final ProductGroupApi productGroupApi;

    private List<ProductGroupDto> productGroupDtoList;

    private ProductGroupDto productGroupDto;

    public ProductGroupServiceImpl(@Value("${product_group_url}") String productGroupUrl, Retrofit retrofit) {
        this.productGroupUrl = productGroupUrl;
        this.productGroupApi = retrofit.create(ProductGroupApi.class);
    }

    @Override
    public List<ProductGroupDto> getAll() {
        Call<List<ProductGroupDto>> productGroupDtoListCall = productGroupApi.getAll(productGroupUrl);

        productGroupDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductGroupDto>> call, Response<List<ProductGroupDto>> response) {
                if (response.isSuccessful()) {
                    productGroupDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка ProductGroupDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение списка ContractorDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroupDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос списка ProductGroupDto", throwable);
            }
        });

        return productGroupDtoList;
    }

    @Override
    public ProductGroupDto getById(Long id) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.getById(productGroupUrl, id);

        productGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductGroupDto> call, Response<ProductGroupDto> response) {
                if (response.isSuccessful()) {
                    productGroupDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра ProductGroupDto по id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на получение экземпляра ProductGroupDto по id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductGroupDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос экземпляра ProductGroupDto по id", throwable);
            }
        });

        return productGroupDto;
    }

    @Override
    public void create(ProductGroupDto dto) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.create(productGroupUrl, productGroupDto);

        productGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductGroupDto> call, Response<ProductGroupDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на создание экземпляра ProductGroupDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на создание экземпляра ProductGroupDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductGroupDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос создания экземпляра ProductGroupDto", throwable);
            }
        });
    }

    @Override
    public void update(ProductGroupDto dto) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.update(productGroupUrl, productGroupDto);

        productGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductGroupDto> call, Response<ProductGroupDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на обновление экземпляра ProductGroupDto");
                } else {
                    log.error("Произошла ошибка при выполнении запроса на обновление экземпляра ProductGroupDto - {}",
                            response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductGroupDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос обновления экземпляра ProductGroupDto", throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.deleteById(productGroupUrl, id);

        productGroupDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductGroupDto> call, Response<ProductGroupDto> response) {
                if (response.isSuccessful()) {
                    log.info("Успешно выполнен запрос на удаление экземпляра ProductGroupDto с id= {}", id);
                } else {
                    log.error("Произошла ошибка при выполнении запроса на удаление экземпляра ProductGroupDto с id= {} - {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductGroupDto> call, Throwable throwable) {
                log.error("Произошла ошибка при получении ответа на запрос удаления экземпляра ProductGroupDto", throwable);
            }
        });
    }
}

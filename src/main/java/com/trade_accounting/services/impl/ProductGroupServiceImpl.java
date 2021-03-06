package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.api.ProductGroupApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
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

        try {
            productGroupDtoList = productGroupDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ProductGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ProductGroupDto - {}", e);
        }

        return productGroupDtoList;
    }

    @Override
    public ProductGroupDto getById(Long id) {
        Call<ProductGroupDto> productGroupDtoCall = productGroupApi.getById(productGroupUrl, id);

        try {
            productGroupDto = productGroupDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение ProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение ProductDto - {}", e);
        }

        return productGroupDto;
    }

    @Override
    public void create(ProductGroupDto dto) {
        Call<Void> productGroupDtoCall = productGroupApi.create(productGroupUrl, productGroupDto);

        try {
            productGroupDtoCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при создании ProductDto - {}", e);
        }
    }

    @Override
    public void update(ProductGroupDto dto) {
        Call<Void> productGroupDtoCall = productGroupApi.update(productGroupUrl, productGroupDto);

        try {
            productGroupDtoCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при обновлении ProductDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productGroupDtoCall = productGroupApi.deleteById(productGroupUrl, id);

        try {
            productGroupDtoCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при удалении ProductDto - {}", e);
        }
    }
}

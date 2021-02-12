package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.api.ProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        try {
            listProducts = productGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ProductDto - {}", e);
        }
        return listProducts;
    }

    @Override
    public List<ProductDto> getAllLite() {
        Call<List<ProductDto>> productGetAllLiteCall = productApi.getAllLite(productUrl);
        try {
            listProducts = productGetAllLiteCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ProductDto (лёгкое дто)");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ProductDto (лёгкое дто) - {}", e);
        }
        return listProducts;
    }

    @Override
    public ProductDto getById(Long id) {
        Call<ProductDto> productGetCall = productApi.getById(productUrl, id);

            try {
                productDto = productGetCall.execute().body();
                log.info("Успешно выполнен запрос на получение ProductDto");
            } catch (IOException e) {
                log.error("Произошла ошибка при выполнении запроса на получение ProductDto - {}", e);
            }
        return productDto;
    }

    @Override
    public void create(ProductDto productDto) {
        Call<Void> productCall = productApi.create(productUrl, productDto);
        try {
            productCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при создании ProductDto - {}", e);
        }
    }

    @Override
    public void update(ProductDto productDto) {
        Call<Void> productUpdateCall = productApi.update(productUrl, productDto);
        try {
            productUpdateCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при обновлении ProductDto - {}", e);
        }

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productDeleteCall = productApi.deleteById(productUrl, id);
        try {
            productDeleteCall.execute();
        } catch (IOException e) {
            log.error("Произошла ошибка при удалении ProductDto - {}", e);
        }
    }

    @Override
    public List<ProductDto> getAllByProductGroupId(Long l) {
        Call<List<ProductDto>> productGetAllCall = productApi.getAllByProductGroup(productUrl, l);
        try {
            listProducts = productGetAllCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ProductDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при получении списка ProductDto - {}", e);
        }
        return listProducts;
    }
}

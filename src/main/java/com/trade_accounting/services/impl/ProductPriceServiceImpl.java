package com.trade_accounting.services.impl;


import com.trade_accounting.controllers.dto.ProductPriceDto;
import com.trade_accounting.services.interfaces.ProductPriceService;
import com.trade_accounting.services.interfaces.api.ProductPriceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class ProductPriceServiceImpl implements ProductPriceService {

    private final String productPriceUrl;

    private final ProductPriceApi productPriceApi;

    private final CallExecuteService<ProductPriceDto> dtoCallExecuteService;

    public ProductPriceServiceImpl(@Value("${productprice_url}") String productPriceUrl
            , Retrofit retrofit
            , CallExecuteService<ProductPriceDto> dtoCallExecuteService) {
        this.productPriceUrl = productPriceUrl;
        this.productPriceApi = retrofit.create(ProductPriceApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ProductPriceDto> getAll() {
        Call<List<ProductPriceDto>> productPriceGetAllLiteCall = productPriceApi.getAll(productPriceUrl);
        return dtoCallExecuteService.callExecuteBodyList(productPriceGetAllLiteCall, ProductPriceDto.class);
    }

    @Override
    public ProductPriceDto getById(Long id) {
        Call<ProductPriceDto> productPriceGetCall = productPriceApi.getById(productPriceUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(productPriceGetCall, ProductPriceDto.class, id);
    }

    @Override
    public void create(ProductPriceDto productPriceDto) {
        Call<Void> productPriceCall = productPriceApi.create(productPriceUrl, productPriceDto);
        dtoCallExecuteService.callExecuteBodyCreate(productPriceCall, ProductPriceDto.class);
    }

    @Override
    public void update(ProductPriceDto productPriceDto) {
        Call<Void> productPriceUpdateCall = productPriceApi.update(productPriceUrl, productPriceDto);
        dtoCallExecuteService.callExecuteBodyUpdate(productPriceUpdateCall, ProductPriceDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productPriceDeleteCall = productPriceApi.deleteById(productPriceUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(productPriceDeleteCall, ProductPriceDto.class, id);
    }
}

package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ProductPriceDto;

import java.util.List;

public interface ProductPriceService {
    List<ProductPriceDto> getAll();

    ProductPriceDto getById(Long id);

    void create(ProductPriceDto productPriceDto);

    void update(ProductPriceDto productPriceDto);

    void deleteById(Long id);
}

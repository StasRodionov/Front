package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductGroupDto;
import com.trade_accounting.services.interfaces.util.PageableService;

import java.util.List;
import java.util.Map;

public interface ProductService extends PageableService<ProductDto> {

    List<ProductDto> getAll();

    ProductDto getById(Long id);

    void create(ProductDto productDto);

    void update(ProductDto productDto);

    void deleteById(Long id);

    List<ProductDto> getAllByProductGroup(ProductGroupDto productGroupDto);

    List<ProductDto> search(String query);

    List<ProductDto> searchByFilter(Map<String, String> query);
}


package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.ProductGroupDto;

import java.util.List;

public interface ProductGroupService {

    List<ProductGroupDto> getAll();

    ProductGroupDto getById(Long id);

    void create(ProductGroupDto dto);

    void update(ProductGroupDto dto);

    void deleteById(Long id);
}

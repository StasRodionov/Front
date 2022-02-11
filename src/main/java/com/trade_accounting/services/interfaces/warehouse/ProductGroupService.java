package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.ProductGroupDto;

import java.util.List;

public interface ProductGroupService {

    List<ProductGroupDto> getAll();

    ProductGroupDto getById(Long id);

    void create(ProductGroupDto dto);

    void update(ProductGroupDto dto);

    void deleteById(Long id);
}

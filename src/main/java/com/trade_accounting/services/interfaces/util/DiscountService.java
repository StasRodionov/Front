package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.DiscountDto;

import java.util.List;

public interface DiscountService {
    List<DiscountDto> getAll();

    DiscountDto getByID(Long id);

    void create(DiscountDto discountDto);

    void update(DiscountDto discountDto);

    void deleteById(Long id);
}

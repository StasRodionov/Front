package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ProductionTargetsDto;

import java.util.List;
import java.util.Map;

public interface ProductionTargetsService {

    List<ProductionTargetsDto> getAll();

    ProductionTargetsDto getById(Long id);

    void create(ProductionTargetsDto dto);

    void update(ProductionTargetsDto dto);

    void deleteById(Long id);

    List<ProductionTargetsDto> searchProductionTargets(Map<String, String> queryProductionTargets);

    List<ProductionTargetsDto> search(String query);

}

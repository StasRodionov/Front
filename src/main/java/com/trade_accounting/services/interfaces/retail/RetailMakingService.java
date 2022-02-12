package com.trade_accounting.services.interfaces.retail;

import com.trade_accounting.models.dto.retail.RetailMakingDto;

import java.util.List;
import java.util.Map;

public interface RetailMakingService {

    List<RetailMakingDto> getAll();

    RetailMakingDto getById(Long id);

    void create(RetailMakingDto retailMakingDto);

    void update(RetailMakingDto retailMakingDto);

    void deleteById(Long id);

    List<RetailMakingDto> searchByFilter(Map<String, String> queryRetailMaking);

    List<RetailMakingDto> search(String search);
}

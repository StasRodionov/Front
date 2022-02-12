package com.trade_accounting.services.interfaces.warehouse;

import com.trade_accounting.models.dto.warehouse.BuyersReturnDto;

import java.util.List;
import java.util.Map;

public interface BuyersReturnService  {

    List<BuyersReturnDto> getAll();

    BuyersReturnDto getById(Long id);

    BuyersReturnDto create(BuyersReturnDto buyersReturnDto);

    void update(BuyersReturnDto buyersReturnDto);

    void deleteById(Long id);

    List<BuyersReturnDto> findBySearch(String search);

    List<BuyersReturnDto> getByContractorId(Long id);

    List<BuyersReturnDto> searchByFilter(Map<String, String> map);

}

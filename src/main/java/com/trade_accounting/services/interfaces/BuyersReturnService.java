package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.ContractDto;

import java.util.List;
import java.util.Map;

public interface BuyersReturnService {

    List<BuyersReturnDto> getAll();

    BuyersReturnDto getById(Long id);

    BuyersReturnDto create(BuyersReturnDto buyersReturnDto);

    void update(BuyersReturnDto buyersReturnDto);

    void deleteById(Long id);

    List<BuyersReturnDto> search(Map<String, String> query);
}

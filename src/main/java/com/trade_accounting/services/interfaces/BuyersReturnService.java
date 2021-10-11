package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.BuyersReturnDto;

import java.util.List;

public interface BuyersReturnService  {

    List<BuyersReturnDto> getAll();

    BuyersReturnDto getById(Long id);

    BuyersReturnDto create(BuyersReturnDto buyersReturnDto);

    void update(BuyersReturnDto buyersReturnDto);

    void deleteById(Long id);

    List<BuyersReturnDto> findBySearch(String search);

    List<BuyersReturnDto> getByContractorId(Long id);
}

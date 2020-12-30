package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ContractDto;

import java.util.List;

public interface ContractService {
    List<ContractDto> getAll();

    ContractDto getById(Long id);

    void create(ContractDto contractDto);

    void update(ContractDto contractDto);

    void deleteById(Long id);
}

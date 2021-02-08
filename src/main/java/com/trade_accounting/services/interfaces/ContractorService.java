package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ContractorDto;

import java.util.List;

public interface ContractorService {

    List<ContractorDto> getAll();
    List<ContractorDto> getAll(String searchTerm);

    ContractorDto getById(Long id);

    void create(ContractorDto contractorDto);

    void update(ContractorDto contractorDto);

    void deleteById(Long id);
}

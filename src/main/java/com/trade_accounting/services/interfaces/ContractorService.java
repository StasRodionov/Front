package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ContractorDto;

import java.util.List;

public interface ContractorService {

    List<ContractorDto> getAll();

    ContractorDto getById(Long id);

    ContractorDto getByEmail(String email);

    void create(ContractorDto contractorDto);

    void update(ContractorDto contractorDto);

    void deleteById(Long id);
}

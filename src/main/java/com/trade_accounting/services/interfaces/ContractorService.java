package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ContractorDto;

import java.util.List;
import java.util.Map;

public interface ContractorService {
    List<ContractorDto> getAll();

    List<ContractorDto> getAllString();

    List<ContractorDto> getAll(String searchTerm);

    ContractorDto getById(Long id);

    List<ContractorDto> searchContractor(Map<String, String> queryContractor);

    void create(ContractorDto contractorDto);

    void update(ContractorDto contractorDto);

    void deleteById(Long id);
}

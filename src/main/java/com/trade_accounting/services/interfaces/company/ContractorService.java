package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.fias.FiasModelDto;

import java.util.List;
import java.util.Map;

public interface ContractorService {

    List<ContractorDto> searchContractor(Map<String, String> queryContractor);

    List<ContractorDto> getAll();

    List<ContractorDto> getAllLite();

    List<ContractorDto> searchByTerm(String searchTerm);

    List<FiasModelDto> getAllAddressByLevel(String searchLevel);

    List<FiasModelDto> getAddressesByGuid(String aoguid);

    ContractorDto getById(Long id);

    void create(ContractorDto contractorDto);

    void update(ContractorDto contractorDto);

    void deleteById(Long id);
}

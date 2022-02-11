package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.ContractorGroupDto;

import java.util.List;

public interface ContractorGroupService {

    List<ContractorGroupDto> getAll();

    ContractorGroupDto getById(Long id);

    ContractorGroupDto getByName(String name);

    void create (ContractorGroupDto dto);

    void update (ContractorGroupDto dto);

    void deleteById (Long id);
}
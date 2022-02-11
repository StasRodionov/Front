package com.trade_accounting.services.interfaces.company;


import com.trade_accounting.models.dto.company.ContractorStatusDto;

import java.util.List;

public interface ContractorStatusService {

    List<ContractorStatusDto> getAll();

    ContractorStatusDto getById(Long id);

}

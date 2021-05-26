package com.trade_accounting.services.interfaces;


import com.trade_accounting.models.dto.StatusDto;

import java.util.List;

public interface StatusService {

    List<StatusDto> getAll();

    StatusDto getById(Long id);

}

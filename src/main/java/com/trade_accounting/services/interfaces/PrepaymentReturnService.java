package com.trade_accounting.services.interfaces;


import com.trade_accounting.models.dto.PrepaymentReturnDto;
import com.trade_accounting.models.dto.PrepayoutDto;

import java.util.List;

public interface PrepaymentReturnService {

    List<PrepaymentReturnDto> getAll();

    PrepaymentReturnDto getById(Long id);

    PrepaymentReturnDto create(PrepaymentReturnDto prepaymentReturnDto);

    void update(PrepaymentReturnDto prepaymentReturnDto);

    void deleteById(Long id);
}

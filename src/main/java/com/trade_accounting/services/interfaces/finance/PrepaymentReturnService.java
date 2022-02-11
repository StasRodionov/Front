package com.trade_accounting.services.interfaces.finance;


import com.trade_accounting.models.dto.finance.PrepaymentReturnDto;

import java.util.List;

public interface PrepaymentReturnService {

    List<PrepaymentReturnDto> getAll();

    PrepaymentReturnDto getById(Long id);

    PrepaymentReturnDto create(PrepaymentReturnDto prepaymentReturnDto);

    void update(PrepaymentReturnDto prepaymentReturnDto);

    void deleteById(Long id);
}

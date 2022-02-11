package com.trade_accounting.services.interfaces.finance;


import com.trade_accounting.models.dto.finance.PrepayoutDto;

import java.util.List;

public interface PrepayoutService {

    List<PrepayoutDto> getAll();

    PrepayoutDto getById(Long id);

    PrepayoutDto create(PrepayoutDto buyersReturnDto);

    void update(PrepayoutDto buyersReturnDto);

    void deleteById(Long id);
}

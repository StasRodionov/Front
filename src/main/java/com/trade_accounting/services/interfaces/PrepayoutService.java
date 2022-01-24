package com.trade_accounting.services.interfaces;


import com.trade_accounting.controllers.dto.PrepayoutDto;

import java.util.List;

public interface PrepayoutService {

    List<PrepayoutDto> getAll();

    PrepayoutDto getById(Long id);

    PrepayoutDto create(PrepayoutDto buyersReturnDto);

    void update(PrepayoutDto buyersReturnDto);

    void deleteById(Long id);
}

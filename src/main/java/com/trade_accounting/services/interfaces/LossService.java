package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.LossDto;

import java.util.List;

public interface LossService {
    List<LossDto> getAll();

    LossDto getById(Long id);

    LossDto create(LossDto lossDto);

    void update(LossDto lossDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

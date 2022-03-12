package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.BonusProgramDto;

import java.util.List;

public interface BonusProgramService {

    List<BonusProgramDto> getAll();

    BonusProgramDto getById(Long id);

    void create(BonusProgramDto bonusProgramDto);

    void update(BonusProgramDto bonusProgramDto);

    void deleteById(Long id);
}

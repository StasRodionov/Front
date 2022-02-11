package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.LegalDetailDto;

import java.util.List;

public interface LegalDetailService {

    List<LegalDetailDto> getAll();

    LegalDetailDto getById(Long id);

    LegalDetailDto create(LegalDetailDto legalDetailDto);

    void update(LegalDetailDto legalDetailDto);

    void deleteById(Long id);
}

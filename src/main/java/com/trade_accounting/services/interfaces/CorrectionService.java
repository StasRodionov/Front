package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CorrectionDto;

import java.util.List;
import java.util.Map;

public interface CorrectionService {

    List<CorrectionDto> getAll();

    CorrectionDto getById(Long id);

    CorrectionDto create(CorrectionDto correctionDto);

    List<CorrectionDto> search(Map<String, String> query);

    void update(CorrectionDto correctionDto);

    void deleteById(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

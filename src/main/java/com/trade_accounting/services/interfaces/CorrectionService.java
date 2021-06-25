package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.InvoiceDto;

import java.util.List;

public interface CorrectionService {

    List<CorrectionDto> getAll();

    CorrectionDto getById(Long id);

    CorrectionDto create(CorrectionDto correctionDto);

    void update(InvoiceDto invoiceDto);

    void deleteById(Long id);
}

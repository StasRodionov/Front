package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceDto;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
    List<InvoiceDto> getAll();

    InvoiceDto getById(Long id);

    List<InvoiceDto> search(Map<String, String> query);

    void create(InvoiceDto invoiceDto);

    void update(InvoiceDto invoiceDto);

    void deleteById(Long id);
}

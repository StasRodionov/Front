package com.trade_accounting.services.interfaces.invoice;

import com.trade_accounting.models.dto.invoice.IssuedInvoiceDto;

import java.util.List;

public interface IssuedInvoiceService {

    List<IssuedInvoiceDto> getAll();

    IssuedInvoiceDto getById(Long id);

    void create(IssuedInvoiceDto issuedInvoiceDto);

    void update(IssuedInvoiceDto issuedInvoiceDto);

    void deleteById(Long id);
}

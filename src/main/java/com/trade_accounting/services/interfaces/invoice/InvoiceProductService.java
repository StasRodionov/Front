package com.trade_accounting.services.interfaces.invoice;


import com.trade_accounting.models.dto.invoice.InvoiceProductDto;

import java.util.List;
import java.util.Map;

public interface InvoiceProductService {
    List<InvoiceProductDto> getAll();

    InvoiceProductDto getById(Long id);

    List<InvoiceProductDto> getByInvoiceId(Long id);

    void create(InvoiceProductDto invoiceProductDto);

    void update(InvoiceProductDto invoiceProductDto);

    void deleteById(Long id);

    List<InvoiceProductDto> search(Map<String, String> query);
}

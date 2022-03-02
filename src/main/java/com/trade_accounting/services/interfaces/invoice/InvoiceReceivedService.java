package com.trade_accounting.services.interfaces.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;

import java.util.List;

public interface InvoiceReceivedService {

    List<InvoiceReceivedDto> getAll();

    InvoiceReceivedDto getById(Long id);

    void create(InvoiceReceivedDto invoiceReceivedDto);

    void update(InvoiceReceivedDto invoiceReceivedDto);

    void deleteById(Long id);

    List<InvoiceReceivedDto> searchByString(String nameFilter);
}

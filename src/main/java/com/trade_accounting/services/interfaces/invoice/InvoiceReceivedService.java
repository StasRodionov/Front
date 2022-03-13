package com.trade_accounting.services.interfaces.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;

import java.util.List;
import java.util.Map;

public interface InvoiceReceivedService {

    List<InvoiceReceivedDto> getAll();

    List<InvoiceReceivedDto> getAll(String typeOfInvoice);

    List<InvoiceReceivedDto> searchByFilter(Map<String, String> queryInvoice);

    List<InvoiceReceivedDto> searchByString(String nameFilter);

    InvoiceReceivedDto getById(Long id);

    void create(InvoiceReceivedDto invoiceReceivedDto);

    void update(InvoiceReceivedDto invoiceReceivedDto);

    void deleteById(Long id);
}

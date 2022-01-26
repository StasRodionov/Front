package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceDto;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface InvoiceService {
    List<InvoiceDto> getAll();

    List<InvoiceDto> getAll(String typeOfInvoice);

    InvoiceDto getById(Long id);

    List<InvoiceDto> search(Map<String, String> query);

    List<InvoiceDto> searchFromDate(LocalDateTime dateTime);

    List<InvoiceDto> findBySearchAndTypeOfInvoice(String search, String typeOfInvoice);

    Response<InvoiceDto> create(InvoiceDto invoiceDto);

    void update(InvoiceDto invoiceDto);

    void deleteById(Long id);

    List<InvoiceDto> getByContractorId(Long id);

    void moveToIsRecyclebin(Long id);

    void restoreFromIsRecyclebin(Long id);
}

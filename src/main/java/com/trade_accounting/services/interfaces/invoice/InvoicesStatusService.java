package com.trade_accounting.services.interfaces.invoice;

import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import java.util.List;


public interface InvoicesStatusService {


    List<InvoicesStatusDto> getAll();

    InvoicesStatusDto getById(Long id);

    void create(InvoicesStatusDto invoicesStatusDto);

    void update(InvoicesStatusDto invoicesStatusDto);

    void deleteById(Long id);

}
